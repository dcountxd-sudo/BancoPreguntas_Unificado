package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

public interface QuestionState {
    void submitForReview(Question question);
    void assignReviewer(Question question);
    void approve(Question question);
    void reject(Question question);
    void publish(Question question);
    void archive(Question question);
    String getName();
    boolean isUsableInAssessment();
}
