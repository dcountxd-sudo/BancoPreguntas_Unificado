package co.unicauca.taller08.decorator;

import co.unicauca.taller08.domain.Question;

/** DECORATOR base: envuelve otro QuestionFeature y delega por defecto; las subclases agregan comportamiento. */
public abstract class QuestionDecorator implements QuestionFeature {
    protected final QuestionFeature wrapped;

    protected QuestionDecorator(QuestionFeature wrapped) {
        if (wrapped == null) throw new IllegalArgumentException("Componente obligatorio");
        this.wrapped = wrapped;
    }

    @Override
    public Question getQuestion() {
        return wrapped.getQuestion();
    }

    @Override
    public String describe() {
        return wrapped.describe();
    }

    @Override
    public double getScore() {
        return wrapped.getScore();
    }
}
