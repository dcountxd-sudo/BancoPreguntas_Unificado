package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

/** Rechazada: el revisor no la aprobó; vuelve a borrador para realizar los ajustes. */
public class RejectedState implements QuestionState {
    @Override
    public String getName() { return "RECHAZADA"; }

    @Override
    public void returnToDraft(Question q) { q.setState(new DraftState()); }
}
