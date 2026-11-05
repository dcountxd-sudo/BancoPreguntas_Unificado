package co.unicauca.taller08.templatemethod;

import java.util.List;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;

public class SingleChoiceEvaluator extends QuestionEvaluator {
    @Override
    protected void validateQuestionType(Question question, List<String> answers) {
        if (question.getType() != QuestionType.MULTIPLE_CHOICE
                && question.getType() != QuestionType.SINGLE_CHOICE) {
            throw new IllegalArgumentException("El evaluador de selección única no corresponde al tipo de pregunta.");
        }
        if (answers.size() != 1) {
            throw new IllegalArgumentException("Debe seleccionar exactamente una respuesta.");
        }
    }

    @Override
    protected boolean compareAnswers(Question question, List<String> answers) {
        return selectedAnswers(answers).equals(correctAnswers(question));
    }
}
