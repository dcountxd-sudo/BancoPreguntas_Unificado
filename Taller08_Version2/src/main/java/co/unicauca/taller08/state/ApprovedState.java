package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

/** Aprobada: el revisor la aprobó; el único paso siguiente es publicarla. */
public class ApprovedState implements QuestionState {
    @Override
    public String getName() { return "APROBADA"; }

    @Override
    public void publish(Question q) { q.setState(new PublishedState()); }
}
