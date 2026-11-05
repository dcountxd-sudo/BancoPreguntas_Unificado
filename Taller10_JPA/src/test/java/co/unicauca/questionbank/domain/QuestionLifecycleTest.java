package co.unicauca.questionbank.domain;

import static org.junit.jupiter.api.Assertions.*;
import static co.unicauca.questionbank.TestData.*;

import co.unicauca.questionbank.domain.exception.BusinessRuleException;
import co.unicauca.questionbank.domain.model.*;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Pure domain tests: no Spring, no JPA, no database. */
class QuestionLifecycleTest {

    private MultipleChoiceQuestion publishedQuestion() {
        MultipleChoiceQuestion q = validMultipleChoice().build();
        q.setId(10L);
        q.submitForReview(); q.assignReviewer(); q.approve(); q.publish();
        return q;
    }

    @Test void newQuestionStartsAsDraft() {
        assertEquals(QuestionStatus.DRAFT, validMultipleChoice().build().getStatus());
    }

    @Test void completeLifecycleReachesArchived() {
        MultipleChoiceQuestion q = publishedQuestion();
        assertEquals(QuestionStatus.PUBLISHED, q.getStatus());
        q.archive();
        assertEquals(QuestionStatus.ARCHIVED, q.getStatus());
    }

    @Test void rejectedQuestionReturnsToDraftAndCanBeResubmitted() {
        MultipleChoiceQuestion q = validMultipleChoice().build();
        q.submitForReview(); q.assignReviewer(); q.reject();
        assertEquals(QuestionStatus.DRAFT, q.getStatus());
        q.submitForReview();
        assertEquals(QuestionStatus.PENDING_REVIEW, q.getStatus());
    }

    @Test void illegalTransitionsAreRejected() {
        MultipleChoiceQuestion draft = validMultipleChoice().build();
        assertThrows(BusinessRuleException.class, draft::approve);
        assertThrows(BusinessRuleException.class, draft::publish);
        assertThrows(BusinessRuleException.class, draft::assignReviewer);
        MultipleChoiceQuestion published = publishedQuestion();
        assertThrows(BusinessRuleException.class, published::submitForReview);
        assertThrows(BusinessRuleException.class, published::reject);
    }

    @Test void onlyDraftsCanBeEdited() {
        MultipleChoiceQuestion q = publishedQuestion();
        assertThrows(BusinessRuleException.class, () -> q.revise("New statement", DifficultyLevel.HARD));
        MultipleChoiceQuestion draft = validMultipleChoice().build();
        draft.revise("New statement", DifficultyLevel.HARD);
        assertEquals("New statement", draft.getStatement());
    }

    @Test void multipleChoiceNeedsFiveOptionsWithOneCorrect() {
        MultipleChoiceQuestion twoOptions = MultipleChoiceQuestion.builder().statement("s").author(author()).topic(topic())
            .competency(competency(1)).option("A", "x", true).option("B", "y", false).build();
        assertThrows(BusinessRuleException.class, twoOptions::submitForReview);
        MultipleChoiceQuestion twoCorrect = MultipleChoiceQuestion.builder().statement("s").author(author()).topic(topic())
            .competency(competency(1)).option("A", "a", true).option("B", "b", true).option("C", "c", false)
            .option("D", "d", false).option("E", "e", false).build();
        assertThrows(BusinessRuleException.class, twoCorrect::submitForReview);
        assertEquals(QuestionStatus.DRAFT, twoCorrect.getStatus());
    }

    @Test void trueFalseNeedsExpectedValue() {
        TrueFalseQuestion q = validTrueFalse().expectedValue(null).build();
        assertThrows(BusinessRuleException.class, q::submitForReview);
        assertDoesNotThrow(() -> validTrueFalse().build().submitForReview());
    }

    @Test void classificationIsRequiredBeforeReview() {
        TrueFalseQuestion noTopic = TrueFalseQuestion.builder().statement("s").author(author()).competency(competency(1)).expectedValue(true).build();
        assertThrows(BusinessRuleException.class, noTopic::submitForReview);
        TrueFalseQuestion noCompetency = TrueFalseQuestion.builder().statement("s").author(author()).topic(topic()).expectedValue(true).build();
        assertThrows(BusinessRuleException.class, noCompetency::submitForReview);
    }

    @Test void questionRequiresAuthorAndStatement() {
        assertThrows(BusinessRuleException.class, () -> TrueFalseQuestion.builder().statement("s").build());
        assertThrows(BusinessRuleException.class, () -> TrueFalseQuestion.builder().author(author()).build());
    }

    @Test void quizOnlyAcceptsPublishedQuestionsWithoutDuplicates() {
        Quiz quiz = new Quiz(null, "Mock exam", teacher().getId());
        MultipleChoiceQuestion published = publishedQuestion();
        quiz.addQuestion(published);
        assertEquals(List.of(10L), quiz.getQuestionIds());
        assertThrows(BusinessRuleException.class, () -> quiz.addQuestion(published));
        assertThrows(BusinessRuleException.class, () -> quiz.addQuestion(validMultipleChoice().build()));   // draft, no identity yet
    }
}
