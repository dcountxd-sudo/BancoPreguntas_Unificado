package co.unicauca.taller08.templatemethod;

import java.util.List;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;

public class TrueFalseEvaluator extends QuestionEvaluator {
    @Override
    protected void validateQuestionType(Question question, List<String> answers) {
        if (question.getType() != QuestionType.TRUE_FALSE) {
            throw new IllegalArgumentException("El evaluador verdadero/falso no corresponde al tipo de pregunta.");
        }
        if (answers.size() != 1) {
            throw new IllegalArgumentException("Debe seleccionar Verdadero o Falso.");
        }
    }

    @Override
    protected boolean compareAnswers(Question question, List<String> answers) {
        return selectedAnswers(answers).equals(correctAnswers(question));
    }
}
