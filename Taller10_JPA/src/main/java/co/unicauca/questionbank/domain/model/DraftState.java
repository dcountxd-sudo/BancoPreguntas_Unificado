package co.unicauca.questionbank.domain.model;

/** Draft: the question is written and can be edited. Sending it to review checks the minimum conditions first. */
public class DraftState implements QuestionState {
    @Override public QuestionStatus status() { return QuestionStatus.DRAFT; }
    @Override public void submitForReview(Question q) { q.ensureReadyForReview(); q.moveTo(new PendingReviewState()); }
    @Override public boolean isEditable() { return true; }
}
