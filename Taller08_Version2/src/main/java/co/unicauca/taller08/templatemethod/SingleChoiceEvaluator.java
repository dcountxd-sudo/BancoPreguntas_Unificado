package co.unicauca.taller08.templatemethod;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;

import java.util.List;

/** Selección única: el estudiante debe seleccionar exactamente una respuesta. */
public class SingleChoiceEvaluator extends QuestionEvaluationTemplate {

    @Override
    protected void validate(Question question, List<String> answers) {
        if (question.getType() != QuestionType.SINGLE_CHOICE) {
            throw new IllegalArgumentException("La pregunta no es de selección única");
        }
        if (answers.size() != 1 || answers.get(0).isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar exactamente una respuesta");
        }
    }

    @Override
    protected boolean compareAnswers(Question question, List<String> answers) {
        String correct = question.getCorrectAnswer();
        if (correct == null || correct.trim().isEmpty()) {
            return false;
        }
        correct = correct.trim();
        if (correct.contains(",")) {
            throw new IllegalStateException(
                    "Una pregunta de selección única no puede tener varias respuestas correctas");
        }
        return correct.equalsIgnoreCase(answers.get(0));
    }
}
