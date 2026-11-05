package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

public abstract class AbstractQuestionState implements QuestionState {
    protected void invalidTransition(String operation) {
        throw new IllegalStateException("No se puede " + operation + " una pregunta en estado " + getName() + ".");
    }

    @Override
    public void submitForReview(Question question) {
        invalidTransition("enviar a revisión");
    }

    @Override
    public void assignReviewer(Question question) {
        invalidTransition("asignar revisor a");
    }

    @Override
    public void approve(Question question) {
        invalidTransition("aprobar");
    }

    @Override
    public void reject(Question question) {
        invalidTransition("rechazar");
    }

    @Override
    public void publish(Question question) {
        invalidTransition("publicar");
    }

    @Override
    public void archive(Question question) {
        invalidTransition("archivar");
    }

    @Override
    public boolean isUsableInAssessment() {
        return false;
    }
}
