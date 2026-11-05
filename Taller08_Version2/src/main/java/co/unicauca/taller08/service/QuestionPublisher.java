package co.unicauca.taller08.service;

import co.unicauca.taller08.domain.Question;

/** Coordina validación y transiciones del ciclo de vida (las reglas viven en los State). */
public class QuestionPublisher {
    private final QuestionValidator validator;

    public QuestionPublisher(QuestionValidator v) {
        validator = v;
    }

    public void submitForReview(Question q) {
        validator.validate(q);
        q.submitForReview();
    }

    public void assignReviewer(Question q) { q.assignReviewer(); }

    public void approve(Question q) { q.approve(); }

    public void reject(Question q) { q.reject(); }

    public void returnToDraft(Question q) { q.returnToDraft(); }

    public void publish(Question q) {
        validator.validate(q);
        q.publish(); // State rechaza la operación si la pregunta no está aprobada
    }

    public void archive(Question q) { q.archive(); }
}
