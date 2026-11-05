package co.unicauca.taller08.templatemethod;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/** Selección múltiple: el estudiante puede seleccionar varias respuestas; deben coincidir todas. */
public class MultipleChoiceEvaluator extends QuestionEvaluationTemplate {

    @Override
    protected void validate(Question question, List<String> answers) {
        if (question.getType() != QuestionType.MULTIPLE_CHOICE) {
            throw new IllegalArgumentException("La pregunta no es de selección múltiple");
        }
        if (answers.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos una respuesta");
        }
        for (String answer : answers) {
            if (answer.isEmpty()) {
                throw new IllegalArgumentException("Hay respuestas vacías en la selección");
            }
        }
    }

    @Override
    protected boolean compareAnswers(Question question, List<String> answers) {
        Set<String> correct = toSet(splitCorrect(question.getCorrectAnswer()));
        Set<String> submitted = toSet(answers);
        if (correct.isEmpty() || submitted.isEmpty()) {
            return false;
        }
        // Debe coincidir exactamente como conjunto (sin faltantes ni sobrantes).
        return correct.equals(submitted);
    }

    private List<String> splitCorrect(String value) {
        return value == null ? java.util.Collections.emptyList()
                : java.util.Arrays.asList(value.split(",", -1));
    }

    /** Normaliza y devuelve un conjunto vacío si hay vacíos o repetidos (selección inválida). */
    private Set<String> toSet(List<String> labels) {
        Set<String> result = new TreeSet<>();
        for (String label : labels) {
            String normalized = label.trim().toUpperCase();
            if (normalized.isEmpty() || !result.add(normalized)) {
                return new TreeSet<>();
            }
        }
        return result;
    }
}
