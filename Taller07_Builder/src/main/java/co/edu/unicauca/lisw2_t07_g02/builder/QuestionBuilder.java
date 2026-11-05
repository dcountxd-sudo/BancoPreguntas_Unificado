package co.edu.unicauca.lisw2_t07_g02.builder;

import co.edu.unicauca.lisw2_t07_g02.domain.Question;

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
