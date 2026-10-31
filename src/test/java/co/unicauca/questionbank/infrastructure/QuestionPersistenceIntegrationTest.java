package co.unicauca.questionbank.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import co.unicauca.questionbank.application.*;
import co.unicauca.questionbank.domain.exception.BusinessRuleException;
import co.unicauca.questionbank.domain.exception.NotFoundException;
import co.unicauca.questionbank.domain.model.*;
import co.unicauca.questionbank.domain.repository.QuestionRepository;
import co.unicauca.questionbank.infrastructure.jpa.record.*;
import co.unicauca.questionbank.infrastructure.jpa.repository.SpringQuestionDataRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

/** Integration tests against an in-memory H2 database: real JPA mappings, real constraints. */
@SpringBootTest(properties = "app.seed-demo-data=false")
class QuestionPersistenceIntegrationTest {
    @Autowired CatalogService catalog;
    @Autowired QuestionService questions;
    @Autowired ReviewService reviews;
    @Autowired QuizService quizzes;
    @Autowired QuestionRepository questionRepository;       // the domain contract
    @Autowired SpringQuestionDataRepository questionData;   // the technology detail, used only to inspect the mapping

    /** Reference data with unique names, so tests do not interfere with each other. */
    private record Fixture(User author, User teacher, Topic topic, Competency design, Competency analysis) {}

    private Fixture fixture() {
        String s = UUID.randomUUID().toString().substring(0, 8);
        return new Fixture(
            catalog.createUser("author-" + s, "Author", "a-" + s + "@x.co", Role.AUTHOR),
            catalog.createUser("teacher-" + s, "Teacher", "t-" + s + "@x.co", Role.TEACHER),
            catalog.createTopic("Topic " + s),
            catalog.createCompetency("Design " + s, "design"),
            catalog.createCompetency("Analysis " + s, "analysis"));
    }

    private Question createMultipleChoice(Fixture f) {
        return questions.createMultipleChoice(f.author().getId(), "Which pattern selects an algorithm at runtime?", DifficultyLevel.MEDIUM,
            f.topic().getId(), Set.of(f.design().getId(), f.analysis().getId()),
            List.of(new AnswerOption("A", "Singleton", false), new AnswerOption("B", "Strategy", true), new AnswerOption("C", "Facade", false),
                    new AnswerOption("D", "Proxy", false), new AnswerOption("E", "Flyweight", false)),
            new QuestionMetadata("Apply", 90, "reviewed by committee", LocalDateTime.now()));
    }

    private Question createTrueFalse(Fixture f) {
        return questions.createTrueFalse(f.author().getId(), "A facade hides subsystem complexity.", DifficultyLevel.EASY,
            f.topic().getId(), Set.of(f.design().getId()), true, null);
    }

    private Question publish(Question q) {
        questions.submitForReview(q.getId());
        reviews.assignReviewer(q.getId()); reviews.approve(q.getId());
        return reviews.publish(q.getId());
    }

    @Test void entitiesAreSavedAndRetrievedWithTheirRelations() {
        Fixture f = fixture();
        Question saved = createMultipleChoice(f);
        Question loaded = questions.findById(saved.getId());

        assertEquals(f.author().getId(), loaded.getAuthor().getId());                   // many-to-one
        assertEquals(f.topic().getName(), loaded.getTopic().getName());                 // many-to-one
        assertEquals(2, loaded.getCompetencies().size());                               // many-to-many
        assertEquals(5, ((MultipleChoiceQuestion) loaded).getOptions().size());         // one-to-many
        assertEquals(1, ((MultipleChoiceQuestion) loaded).getOptions().stream().filter(AnswerOption::isCorrect).count());
        assertEquals("Apply", loaded.getMetadata().getCognitiveLevel());                // one-to-one
        assertEquals(QuestionStatus.DRAFT, loaded.getStatus());
    }

    @Test void inheritanceStoresAndRecoversEachQuestionType() {
        Fixture f = fixture();
        Question mc = createMultipleChoice(f);
        Question tf = createTrueFalse(f);
        List<Question> byAuthor = questions.findByAuthor(f.author().getId());

        assertEquals(2, byAuthor.size());
        assertInstanceOf(MultipleChoiceQuestion.class, questions.findById(mc.getId()));
        assertInstanceOf(TrueFalseQuestion.class, questions.findById(tf.getId()));
        assertEquals(Boolean.TRUE, ((TrueFalseQuestion) questions.findById(tf.getId())).getExpectedValue());
    }

    @Test void lifecycleStateIsPersisted() {
        Fixture f = fixture();
        Question q = createMultipleChoice(f);
        questions.submitForReview(q.getId());
        assertEquals(QuestionStatus.PENDING_REVIEW, questions.findById(q.getId()).getStatus());
        assertTrue(questions.findByStatus(QuestionStatus.PENDING_REVIEW).stream().anyMatch(x -> x.getId().equals(q.getId())));
    }

    @Test void illegalTransitionIsRejectedAndStatusIsUnchanged() {
        Fixture f = fixture();
        Question q = createMultipleChoice(f);
        assertThrows(BusinessRuleException.class, () -> reviews.approve(q.getId()));
        assertEquals(QuestionStatus.DRAFT, questions.findById(q.getId()).getStatus());
    }

    @Test void incompleteQuestionCannotBeSentToReview() {
        Fixture f = fixture();
        Question q = questions.createMultipleChoice(f.author().getId(), "Only two options", DifficultyLevel.EASY, f.topic().getId(),
            Set.of(f.design().getId()), List.of(new AnswerOption("A", "x", true), new AnswerOption("B", "y", false)), null);
        assertThrows(BusinessRuleException.class, () -> questions.submitForReview(q.getId()));
        assertEquals(QuestionStatus.DRAFT, questions.findById(q.getId()).getStatus());
    }

    @Test void foreignKeysPreventInvalidRelations() {
        Fixture f = fixture();
        assertThrows(NotFoundException.class, () -> questions.createTrueFalse(987654L, "s", DifficultyLevel.EASY, f.topic().getId(), Set.of(f.design().getId()), true, null));
        assertThrows(NotFoundException.class, () -> questions.createTrueFalse(f.author().getId(), "s", DifficultyLevel.EASY, 987654L, Set.of(f.design().getId()), true, null));
        // a record without author is rejected by the NOT NULL foreign key even if the service is bypassed
        TrueFalseQuestionRecord orphan = new TrueFalseQuestionRecord();
        orphan.setStatement("s"); orphan.setDifficulty(DifficultyLevel.EASY); orphan.setStatus(QuestionStatus.DRAFT);
        assertThrows(DataAccessException.class, () -> questionData.saveAndFlush(orphan));
    }

    @Test void uniqueConstraintRejectsDuplicatedUsername() {
        String name = "dup-" + UUID.randomUUID();
        catalog.createUser(name, "One", "one@x.co", Role.AUTHOR);
        assertThrows(DataAccessException.class, () -> catalog.createUser(name, "Two", "two@x.co", Role.AUTHOR));
    }

    @Test void publishedQuestionsCanBeQueriedByCompetency() {
        Fixture f = fixture();
        Question published = publish(createMultipleChoice(f));
        Question draft = createTrueFalse(f);
        List<Question> found = questions.findPublishedByCompetency(f.design().getId());
        assertTrue(found.stream().anyMatch(q -> q.getId().equals(published.getId())));
        assertFalse(found.stream().anyMatch(q -> q.getId().equals(draft.getId())));
    }

    @Test void quizOnlyAcceptsPublishedQuestions() {
        Fixture f = fixture();
        Question draft = createMultipleChoice(f);
        assertThrows(BusinessRuleException.class, () -> quizzes.create(f.teacher().getId(), "Mock", List.of(draft.getId())));

        Question published = publish(createTrueFalse(f));
        Quiz quiz = quizzes.create(f.teacher().getId(), "Mock exam", List.of(published.getId()));
        Quiz loaded = quizzes.findById(quiz.getId());
        assertEquals(List.of(published.getId()), loaded.getQuestionIds());
        assertEquals(f.teacher().getId(), loaded.getCreatorId());
    }

    @Test void repositoryContractWorksThroughTheDomainInterface() {
        Fixture f = fixture();
        Question saved = createTrueFalse(f);
        assertTrue(questionRepository.findById(saved.getId()).isPresent());
        assertTrue(questionRepository.findById(-1L).isEmpty());
    }

    @Test void lazyAssociationsFailOutsideATransaction() {
        Fixture f = fixture();
        Question saved = createMultipleChoice(f);
        QuestionRecord record = questionData.findById(saved.getId()).orElseThrow();
        assertThrows(LazyInitializationException.class, () -> record.getCompetencies().size());
        // the domain object is safe: the mapper loaded everything inside the repository transaction
        assertEquals(2, questions.findById(saved.getId()).getCompetencies().size());
    }
}
