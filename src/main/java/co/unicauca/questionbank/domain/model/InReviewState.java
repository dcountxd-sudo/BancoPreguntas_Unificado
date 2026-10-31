package co.unicauca.questionbank.domain.model;

/** In review: the reviewer analyses the question and approves or rejects it. */
public class InReviewState implements QuestionState {
    @Override public QuestionStatus status() { return QuestionStatus.IN_REVIEW; }
    @Override public void approve(Question q) { q.moveTo(new ApprovedState()); }
    @Override public void reject(Question q) { q.moveTo(new DraftState()); }
}
