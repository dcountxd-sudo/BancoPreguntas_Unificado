package co.unicauca.questionbank.domain.model;

/** Published: available for quizzes. It can only be archived. */
public class PublishedState implements QuestionState {
    @Override public QuestionStatus status() { return QuestionStatus.PUBLISHED; }
    @Override public void archive(Question q) { q.moveTo(new ArchivedState()); }
}
