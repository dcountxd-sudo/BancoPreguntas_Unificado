package co.unicauca.taller08.builder;

import co.unicauca.taller08.domain.DifficultyLevel;
import co.unicauca.taller08.domain.Option;
import co.unicauca.taller08.domain.Question;

import java.util.List;

public abstract class QuestionBuilder {

    protected Question question;

    public abstract void reset();

    public abstract void buildStatement(String statement);

    public abstract void buildContext(String context);

    public abstract void buildDifficulty(DifficultyLevel difficulty);

    public abstract void buildCategory(String category);

    public abstract void buildSubtopic(String subtopic);

    public abstract void buildCompetence(String competence);

    public abstract void buildAuthor(String author);

    public abstract void buildJustification(String justification);

    public abstract void buildSource(String source);

    public abstract void buildAnswers(List<Option> options, String correctAnswer);

    public abstract Question build();

    protected void validateCommon() {

        if (isBlank(question.getStatement())) {
            throw new IllegalStateException("La pregunta debe tener enunciado");
        }

        if (isBlank(question.getContext())) {
            throw new IllegalStateException("La pregunta debe tener contexto");
        }

        if (question.getDifficulty() == null) {
            throw new IllegalStateException("La pregunta debe tener nivel de dificultad");
        }

        if (isBlank(question.getCategory())) {
            throw new IllegalStateException("La pregunta debe tener categoria");
        }

        if (isBlank(question.getSubtopic())) {
            throw new IllegalStateException("La pregunta debe tener subtema");
        }

        if (isBlank(question.getCompetence())) {
            throw new IllegalStateException("La pregunta debe tener competencia");
        }

        if (isBlank(question.getAuthor())) {
            throw new IllegalStateException("La pregunta debe tener autor");
        }

        if (isBlank(question.getJustification())) {
            throw new IllegalStateException("La pregunta debe tener justificacion");
        }

        if (isBlank(question.getSource())) {
            throw new IllegalStateException("La pregunta debe tener fuente o bibliografia");
        }
    }

    protected static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}