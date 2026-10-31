package co.unicauca.questionbank.domain.model;

/** Pending review: waiting for the administrator to assign at least one reviewer. */
public class PendingReviewState implements QuestionState {
    @Override public QuestionStatus status() { return QuestionStatus.PENDING_REVIEW; }
    @Override public void assignReviewer(Question q) { q.moveTo(new InReviewState()); }
}
