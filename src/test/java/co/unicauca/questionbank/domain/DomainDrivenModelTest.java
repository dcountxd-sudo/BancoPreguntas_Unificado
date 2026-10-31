package co.unicauca.questionbank.domain;

import static co.unicauca.questionbank.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

import co.unicauca.questionbank.domain.exception.BusinessRuleException;
import co.unicauca.questionbank.domain.model.*;
import co.unicauca.questionbank.domain.service.QuizAssembler;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Tests of the tactical DDD building blocks: entities, value objects and the domain service. Pure Java. */
class DomainDrivenModelTest {
    private final QuizAssembler assembler = new QuizAssembler();

    private MultipleChoiceQuestion published(long id) {
        MultipleChoiceQuestion q = validMultipleChoice().build();
        q.setId(id);
        q.submitForReview(); q.assignReviewer(); q.approve(); q.publish();
        return q;
    }

    @Test void entitiesAreEqualByIdentityNotByValues() {
        assertEquals(new Topic(1L, "A"), new Topic(1L, "B"));
        assertNotEquals(new Topic(1L, "A"), new Topic(2L, "A"));
        Topic unsaved = new Topic(null, "A");
        assertNotEquals(unsaved, new Topic(null, "A"));   // no identity yet: only the same instance is equal
        assertEquals(unsaved, unsaved);
        assertNotEquals(new Topic(1L, "A"), new Competency(1L, "A", null));   // same id, different type
    }

    @Test void valueObjectsAreEqualByValue() {
        assertEquals(new AnswerOption("A", "x", true), new AnswerOption("A", "x", true));
        assertNotEquals(new AnswerOption("A", "x", true), new AnswerOption("A", "x", false));
        LocalDateTime now = LocalDateTime.now();
        assertEquals(new QuestionMetadata("Apply", 60, null, now), new QuestionMetadata("Apply", 60, null, now));
        assertEquals(new QuestionMetadata("Apply", 60, null, now).hashCode(), new QuestionMetadata("Apply", 60, null, now).hashCode());
    }

    @Test void rolesDecideWhoMayAuthorOrCreateQuizzes() {
        assertTrue(author().canAuthorQuestions());
        assertFalse(teacher().canAuthorQuestions());
        assertTrue(teacher().canCreateQuizzes());
        assertFalse(author().canCreateQuizzes());
    }

    @Test void onlyAuthorsCanCreateQuestionsAtDomainLevel() {
        assertThrows(BusinessRuleException.class, () -> validTrueFalse().author(teacher()).build());
        assertDoesNotThrow(() -> validTrueFalse().author(author()).build());
    }

    @Test void stateIsCheckedBeforeContentWhenSubmitting() {
        MultipleChoiceQuestion q = published(10L);
        BusinessRuleException e = assertThrows(BusinessRuleException.class, q::submitForReview);
        assertTrue(e.getMessage().contains("PUBLISHED"), e.getMessage());
    }

    @Test void quizAssemblerBuildsQuizReferencingByIdentity() {
        Quiz quiz = assembler.assemble(teacher(), "Mock", List.of(published(10L), published(11L)));
        assertEquals(List.of(10L, 11L), quiz.getQuestionIds());
        assertEquals(teacher().getId(), quiz.getCreatorId());
    }

    @Test void quizAssemblerRejectsNonTeachersAndUnpublishedQuestions() {
        assertThrows(BusinessRuleException.class, () -> assembler.assemble(author(), "Mock", List.of(published(10L))));
        MultipleChoiceQuestion draft = validMultipleChoice().build();
        draft.setId(12L);
        assertThrows(BusinessRuleException.class, () -> assembler.assemble(teacher(), "Mock", List.of(draft)));
    }
}
