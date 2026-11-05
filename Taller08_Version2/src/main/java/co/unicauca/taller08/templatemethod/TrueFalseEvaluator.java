package co.unicauca.taller08.templatemethod;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;

import java.util.List;

/** Verdadero/Falso: el estudiante selecciona Verdadero o Falso. */
public class TrueFalseEvaluator extends QuestionEvaluationTemplate {

    @Override
    protected void validate(Question question, List<String> answers) {
        if (question.getType() != QuestionType.TRUE_FALSE) {
            throw new IllegalArgumentException("La pregunta no es verdadero/falso");
        }
        if (answers.size() != 1) {
            throw new IllegalArgumentException("Debe responder una sola opción: verdadero o falso");
        }
        String submitted = normalize(answers.get(0));
        if (!submitted.equals("verdadero") && !submitted.equals("falso")) {
            throw new IllegalArgumentException("La respuesta debe ser V, F, verdadero o falso");
        }
    }

    @Override
    protected boolean compareAnswers(Question question, List<String> answers) {
        String correct = normalize(question.getCorrectAnswer());
        if (!correct.equals("verdadero") && !correct.equals("falso")) {
            throw new IllegalStateException("La respuesta correcta debe ser verdadero o falso");
        }
        return correct.equals(normalize(answers.get(0)));
    }

    private String normalize(String value) {
        if (value == null) return "";
        String n = value.trim().toLowerCase();
        if (n.equals("v") || n.equals("true") || n.equals("verdadero")) return "verdadero";
        if (n.equals("f") || n.equals("false") || n.equals("falso")) return "falso";
        return n;
    }
}
