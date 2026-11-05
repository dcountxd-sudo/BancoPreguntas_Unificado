package co.unicauca.questionbank.domain.model;

/** Approved: the reviewer accepted it; it still has to be published before it can be used in a quiz. */
public class ApprovedState implements QuestionState {
    @Override public QuestionStatus status() { return QuestionStatus.APPROVED; }
    @Override public void publish(Question q) { q.moveTo(new PublishedState()); }
}
