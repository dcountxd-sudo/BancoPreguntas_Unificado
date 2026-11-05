package co.unicauca.taller08.director;

import co.unicauca.taller08.builder.QuestionBuilder;
import co.unicauca.taller08.domain.Question;

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
