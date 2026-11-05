package co.unicauca.questionbank.domain.model;

/** Archived: final state, no transition is allowed. */
public class ArchivedState implements QuestionState {
    @Override public QuestionStatus status() { return QuestionStatus.ARCHIVED; }
}
