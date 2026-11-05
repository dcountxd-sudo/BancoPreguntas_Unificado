package co.edu.unicauca.lisw2_t07_g02.director;

import co.edu.unicauca.lisw2_t07_g02.builder.QuestionBuilder;
import co.edu.unicauca.lisw2_t07_g02.domain.Question;

public class QuestionDirector {
    private QuestionBuilder questionBuilder;

    public void setQuestionBuilder(QuestionBuilder questionBuilder) {
        this.questionBuilder = questionBuilder;
    }

    public Question getQuestion() {
        if (questionBuilder == null) {
            throw new IllegalStateException("Debe configurar un builder antes de construir la pregunta.");
        }
        return questionBuilder.getQuestion();
    }

    public void construirPregunta() {
        if (questionBuilder == null) {
            throw new IllegalStateException("Debe configurar un builder antes de construir la pregunta.");
        }

        questionBuilder.crearNuevaPregunta();
        questionBuilder.buildType();
        questionBuilder.buildDifficultyLevel();
        questionBuilder.buildStatement();
        questionBuilder.buildOptions();
        questionBuilder.getQuestion().validate();
    }

    public Question construir() {
        construirPregunta();
        return getQuestion();
    }
}
