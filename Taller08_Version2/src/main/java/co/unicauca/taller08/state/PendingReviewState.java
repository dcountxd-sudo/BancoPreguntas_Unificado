package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

/** Pendiente de revisión: contenido completo, a la espera de que se asigne un revisor. */
public class PendingReviewState implements QuestionState {
    @Override
    public String getName() { return "PENDIENTE DE REVISIÓN"; }

    @Override
    public void assignReviewer(Question q) { q.setState(new ReviewState()); }
}
