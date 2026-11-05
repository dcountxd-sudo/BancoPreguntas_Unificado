package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionStateTest {

    private Question inReview() {
        Question q = new Question();
        q.submitForReview();
        q.assignReviewer();
        return q;
    }

    @Test
    void newQuestionStartsInDraft() {
        assertEquals("BORRADOR", new Question().getStatus());
    }

    @Test
    void draftGoesToPendingReviewThenReview() {
        Question q = new Question();
        q.submitForReview();
        assertEquals("PENDIENTE DE REVISIÓN", q.getStatus());
        q.assignReviewer();
        assertEquals("EN REVISIÓN", q.getStatus());
    }

    @Test
    void reviewCanBeApprovedAndThenPublishedAndArchived() {
        Question q = inReview();
        q.approve();
        assertEquals("APROBADA", q.getStatus());
        q.publish();
        assertEquals("PUBLICADA", q.getStatus());
        q.archive();
        assertEquals("ARCHIVADA", q.getStatus());
    }

    @Test
    void rejectedQuestionReturnsToDraft() {
        Question q = inReview();
        q.reject();
        assertEquals("RECHAZADA", q.getStatus());
        q.returnToDraft();
        assertEquals("BORRADOR", q.getStatus());
    }

    @Test
    void draftCannotBePublishedOrApprovedDirectly() {
        Question q = new Question();
        assertThrows(IllegalStateException.class, q::publish);
        assertThrows(IllegalStateException.class, q::approve);
        assertEquals("BORRADOR", q.getStatus());
    }

    @Test
    void draftCannotSkipPendingReview() {
        Question q = new Question();
        assertThrows(IllegalStateException.class, q::assignReviewer);
    }

    @Test
    void publishedCannotReturnToDraft() {
        Question q = inReview();
        q.approve();
        q.publish();
        assertThrows(IllegalStateException.class, q::returnToDraft);
        assertThrows(IllegalStateException.class, q::submitForReview);
        assertEquals("PUBLICADA", q.getStatus());
    }

    @Test
    void approvedCannotBeArchivedWithoutPublishing() {
        Question q = inReview();
        q.approve();
        assertThrows(IllegalStateException.class, q::archive);
    }

    @Test
    void archivedHasNoTransitions() {
        Question q = inReview();
        q.approve();
        q.publish();
        q.archive();
        assertThrows(IllegalStateException.class, q::publish);
        assertThrows(IllegalStateException.class, q::returnToDraft);
        assertThrows(IllegalStateException.class, q::submitForReview);
    }

    @Test
    void rejectedCannotBeApprovedOrPublished() {
        Question q = inReview();
        q.reject();
        assertThrows(IllegalStateException.class, q::approve);
        assertThrows(IllegalStateException.class, q::publish);
    }

    @Test
    void nullStateIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Question().setState(null));
    }
}
