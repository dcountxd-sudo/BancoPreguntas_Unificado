package co.unicauca.questionbank.application;

import static org.junit.jupiter.api.Assertions.*;
import static co.unicauca.questionbank.TestData.validOptions;

import co.unicauca.questionbank.application.InMemoryRepositories.*;
import co.unicauca.questionbank.domain.exception.BusinessRuleException;
import co.unicauca.questionbank.domain.exception.NotFoundException;
import co.unicauca.questionbank.domain.model.*;
import co.unicauca.questionbank.domain.service.QuizAssembler;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Application-layer tests that use only the repository contracts (in-memory fakes, no Spring, no JPA). */
class ApplicationServicesTest {
    private CatalogService catalog;
    private QuestionService questions;
    private ReviewService reviews;
    private QuizService quizzes;
    private User author, teacher;
    private Topic topic;
    private Competency competency;

    @BeforeEach void setUp() {
        Users users = new Users(); Topics topics = new Topics(); Competencies competencies = new Competencies();
        Questions questionRepo = new Questions();
        catalog = new CatalogService(users, topics, competencies);
        questions = new QuestionService(questionRepo, users, topics, competencies);
        reviews = new ReviewService(questionRepo);
        quizzes = new QuizService(new Quizzes(), questionRepo, users, new QuizAssembler());
        author = catalog.createUser("ana", "Ana", "ana@x.co", Role.AUTHOR);
        teacher = catalog.createUser("tom", "Tom", "tom@x.co", Role.TEACHER);
        topic = catalog.createTopic("Architecture");
        competency = catalog.createCompetency("Design", null);
    }

    private Question newQuestion(List<AnswerOption> options) {
        return questions.createMultipleChoice(author.getId(), "Which pattern?", DifficultyLevel.EASY, topic.getId(),
            Set.of(competency.getId()), options, null);
    }

    @Test void createsDraftQuestionWithItsRelations() {
        Question q = newQuestion(validOptions());
        assertNotNull(q.getId());
        assertEquals(QuestionStatus.DRAFT, q.getStatus());
        assertEquals(author.getId(), q.getAuthor().getId());
        assertEquals(1, q.getCompetencies().size());
    }

    @Test void rejectsNonexistentAuthorTopicAndCompetency() {
        assertThrows(NotFoundException.class, () -> questions.createTrueFalse(999L, "s", DifficultyLevel.EASY, topic.getId(), Set.of(competency.getId()), true, null));
        assertThrows(NotFoundException.class, () -> questions.createTrueFalse(author.getId(), "s", DifficultyLevel.EASY, 999L, Set.of(competency.getId()), true, null));
        assertThrows(NotFoundException.class, () -> questions.createTrueFalse(author.getId(), "s", DifficultyLevel.EASY, topic.getId(), Set.of(999L), true, null));
    }

    @Test void onlyAuthorsCreateQuestions() {
        assertThrows(BusinessRuleException.class, () -> questions.createTrueFalse(teacher.getId(), "s", DifficultyLevel.EASY, topic.getId(), Set.of(competency.getId()), true, null));
    }

    @Test void submitForReviewChangesStatusOnlyWhenRulesPass() {
        Question valid = newQuestion(validOptions());
        assertEquals(QuestionStatus.PENDING_REVIEW, questions.submitForReview(valid.getId()).getStatus());

        Question incomplete = newQuestion(List.of(new AnswerOption("A", "x", true)));
        assertThrows(BusinessRuleException.class, () -> questions.submitForReview(incomplete.getId()));
        assertEquals(QuestionStatus.DRAFT, questions.findById(incomplete.getId()).getStatus());
    }

    @Test void reviewFlowFollowsTheLifecycle() {
        Question q = newQuestion(validOptions());
        assertThrows(BusinessRuleException.class, () -> reviews.approve(q.getId()));
        questions.submitForReview(q.getId());
        reviews.assignReviewer(q.getId());
        assertEquals(QuestionStatus.DRAFT, reviews.reject(q.getId()).getStatus());
        questions.submitForReview(q.getId());
        reviews.assignReviewer(q.getId());
        reviews.approve(q.getId());
        assertEquals(QuestionStatus.PUBLISHED, reviews.publish(q.getId()).getStatus());
    }

    @Test void quizRejectsUnpublishedQuestionsAndNonTeachers() {
        Question draft = newQuestion(validOptions());
        assertThrows(BusinessRuleException.class, () -> quizzes.create(teacher.getId(), "Mock", List.of(draft.getId())));
        questions.submitForReview(draft.getId());
        reviews.assignReviewer(draft.getId()); reviews.approve(draft.getId()); reviews.publish(draft.getId());
        assertEquals(1, quizzes.create(teacher.getId(), "Mock", List.of(draft.getId())).getQuestionIds().size());
        assertThrows(BusinessRuleException.class, () -> quizzes.create(author.getId(), "Mock", List.of(draft.getId())));
    }
}
