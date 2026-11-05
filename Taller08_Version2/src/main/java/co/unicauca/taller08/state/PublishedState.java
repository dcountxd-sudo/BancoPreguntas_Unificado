package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

/** Publicada: disponible para su uso en pruebas. */
public class PublishedState implements QuestionState {
    @Override
    public String getName() { return "PUBLICADA"; }

    @Override
    public void archive(Question q) { q.setState(new ArchivedState()); }
}
