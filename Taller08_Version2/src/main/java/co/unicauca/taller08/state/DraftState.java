package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

/** Borrador: la pregunta se crea y se guarda. Al completar el contenido queda pendiente de revisión. */
public class DraftState implements QuestionState {
    @Override
    public String getName() { return "BORRADOR"; }

    @Override
    public void submitForReview(Question q) { q.setState(new PendingReviewState()); }
}
