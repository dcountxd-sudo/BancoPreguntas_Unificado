package co.unicauca.taller08.templatemethod;

import java.util.List;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;

public class MultipleAnswerEvaluator extends QuestionEvaluator {
    @Override
    protected void validateQuestionType(Question question, List<String> answers) {
        if (question.getType() != QuestionType.MULTIPLE_ANSWER) {
            throw new IllegalArgumentException("El evaluador de selección múltiple no corresponde al tipo de pregunta.");
        }
    }

    @Override
    protected boolean compareAnswers(Question question, List<String> answers) {
        return selectedAnswers(answers).equals(correctAnswers(question));
    }
}
