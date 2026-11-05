package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

/** En revisión: el revisor analiza la pregunta y decide si la aprueba o la rechaza. */
public class ReviewState implements QuestionState {
    @Override
    public String getName() { return "EN REVISIÓN"; }

    @Override
    public void approve(Question q) { q.setState(new ApprovedState()); }

    @Override
    public void reject(Question q) { q.setState(new RejectedState()); }
}
