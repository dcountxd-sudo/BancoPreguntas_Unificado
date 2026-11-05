
package co.unicauca.taller08.builder;

import co.unicauca.taller08.domain.DifficultyLevel;
import co.unicauca.taller08.domain.Option;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SingleChoiceQuestionBuilder extends QuestionBuilder {

    public SingleChoiceQuestionBuilder() {
        reset();
    }

    @Override
    public void reset() {
        question = new Question();
        question.setType(QuestionType.SINGLE_CHOICE);
    }

    @Override
    public void buildStatement(String statement) {
        question.setStatement(statement);
    }

    @Override
    public void buildContext(String context) {
        question.setContext(context);
    }

    @Override
    public void buildDifficulty(DifficultyLevel difficulty) {
        question.setDifficulty(difficulty);
    }

    @Override
    public void buildCategory(String category) {
        question.setCategory(category);
    }

    @Override
    public void buildSubtopic(String subtopic) {
        question.setSubtopic(subtopic);
    }

    @Override
    public void buildCompetence(String competence) {
        question.setCompetence(competence);
    }

    @Override
    public void buildAuthor(String author) {
        question.setAuthor(author);
    }

    @Override
    public void buildJustification(String justification) {
        question.setJustification(justification);
    }

    @Override
    public void buildSource(String source) {
        question.setSource(source);
    }

    @Override
    public void buildAnswers(List<Option> options, String correctAnswer) {
        question.setOptions(options);
        question.setCorrectAnswer(correctAnswer);
    }

    @Override
    public Question build() {
        validateCommon();
        validateSpecific();

        Question result = question;
        reset();

        return result;
    }

    private void validateSpecific() {
        List<Option> options = question.getOptions();

        if (options.size() < 2) {
            throw new IllegalStateException(
                    "Una pregunta de selección única debe tener al menos dos opciones");
        }

        Set<String> labels = new HashSet<>();

        for (Option option : options) {
            if (option == null
                    || isBlank(option.getLabel())
                    || isBlank(option.getText())) {
                throw new IllegalStateException(
                        "Todas las opciones deben tener etiqueta y texto");
            }

            String label = option.getLabel().trim().toUpperCase();

            if (!labels.add(label)) {
                throw new IllegalStateException(
                        "Hay opciones con la etiqueta repetida: " + label);
            }
        }

        String correct = question.getCorrectAnswer();

        if (isBlank(correct)) {
            throw new IllegalStateException(
                    "Debe indicar una respuesta correcta");
        }

        // Selección única: solo se admite una etiqueta correcta.
        if (correct.contains(",")) {
            throw new IllegalStateException(
                    "Una pregunta de selección única solo admite una respuesta correcta");
        }

        String normalized = correct.trim().toUpperCase();

        if (!labels.contains(normalized)) {
            throw new IllegalStateException(
                    "La respuesta correcta '" + normalized
                            + "' no corresponde a una opción existente");
        }
    }
}
