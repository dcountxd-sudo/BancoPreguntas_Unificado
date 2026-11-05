package co.unicauca.taller08.builder;

import co.unicauca.taller08.domain.DifficultyLevel;
import co.unicauca.taller08.domain.Option;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultipleChoiceQuestionBuilder extends QuestionBuilder {

    private static final int MIN_OPTIONS = 2;

    public MultipleChoiceQuestionBuilder() {
        reset();
    }

    public void reset() {
        question = new Question();
        question.setType(QuestionType.MULTIPLE_CHOICE);
    }

    public void buildStatement(String statement) {
        question.setStatement(statement);
    }

    public void buildContext(String context) {
        question.setContext(context);
    }

    public void buildDifficulty(DifficultyLevel difficulty) {
        question.setDifficulty(difficulty);
    }

    public void buildCategory(String category) {
        question.setCategory(category);
    }

    public void buildSubtopic(String subtopic) {
        question.setSubtopic(subtopic);
    }

    public void buildCompetence(String competence) {
        question.setCompetence(competence);
    }

    public void buildAuthor(String author) {
        question.setAuthor(author);
    }

    public void buildJustification(String justification) {
        question.setJustification(justification);
    }

    public void buildSource(String source) {
        question.setSource(source);
    }

    public void buildAnswers(List<Option> options, String correctAnswer) {
        question.setOptions(options);
        question.setCorrectAnswer(correctAnswer);
    }

    public Question build() {
        validateCommon();
        validateSpecific();

        Question result = question;

        reset(); // queda listo para construir otra pregunta

        return result;
    }

    private void validateSpecific() {

        List<Option> options = question.getOptions();

        if (options.size() < MIN_OPTIONS) {
            throw new IllegalStateException(
                    "Una pregunta de seleccion multiple debe tener al menos "
                    + MIN_OPTIONS + " opciones");
        }

        Set<String> labels = new HashSet<>();

        for (Option o : options) {

            if (isBlank(o.getLabel()) || isBlank(o.getText())) {
                throw new IllegalStateException(
                        "Todas las opciones deben tener etiqueta y texto");
            }

            if (!labels.add(o.getLabel().trim().toUpperCase())) {
                throw new IllegalStateException(
                        "Hay opciones con la etiqueta repetida: "
                        + o.getLabel());
            }
        }

        String correct = question.getCorrectAnswer();
        if (isBlank(correct)) {
            throw new IllegalStateException("Debe indicar al menos una respuesta correcta");
        }
        Set<String> correctLabels = new HashSet<>();
        for (String label : correct.split(",")) {
            String normalized = label.trim().toUpperCase();
            if (!labels.contains(normalized)) {
                throw new IllegalStateException(
                        "La respuesta correcta '" + normalized + "' no corresponde a una opción existente");
            }
            if (!correctLabels.add(normalized)) {
                throw new IllegalStateException("Hay etiquetas correctas repetidas: " + normalized);
            }
        }
    }
}