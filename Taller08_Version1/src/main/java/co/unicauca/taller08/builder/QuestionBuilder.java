package co.unicauca.taller08.builder;

import co.unicauca.taller08.domain.Question;

public abstract class QuestionBuilder {
    protected Question question;

    public Question getQuestion() {
        return question;
    }

    public void crearNuevaPregunta() {
        question = new Question();
    }

    public abstract void buildType();

    public abstract void buildDifficultyLevel();

    public abstract void buildStatement();

    public abstract void buildOptions();

    public Question buildQuestion() {
        crearNuevaPregunta();
        buildType();
        buildDifficultyLevel();
        buildStatement();
        buildOptions();
        question.validate();
        return question;
    }
}
