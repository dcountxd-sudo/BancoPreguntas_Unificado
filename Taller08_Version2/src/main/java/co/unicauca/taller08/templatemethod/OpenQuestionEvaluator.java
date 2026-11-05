package co.unicauca.taller08.templatemethod;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;

import java.util.Collections;
import java.util.List;

/** Pregunta abierta: la respuesta completa (puede contener comas) se compara con la esperada. */
public class OpenQuestionEvaluator extends QuestionEvaluationTemplate {

    @Override
    protected List<String> parseAnswers(String answer) {
        return Collections.singletonList(answer.trim());
    }

    @Override
    protected void validate(Question question, List<String> answers) {
        if (question.getType() != QuestionType.OPEN) {
            throw new IllegalArgumentException("La pregunta no es abierta");
        }
        if (answers.size() != 1 || answers.get(0).isEmpty()) {
            throw new IllegalArgumentException("La respuesta es obligatoria");
        }
    }

    @Override
    protected boolean compareAnswers(Question question, List<String> answers) {
        String correct = question.getCorrectAnswer();
        if (correct == null || correct.trim().isEmpty()) {
            throw new IllegalStateException("La pregunta abierta no tiene una respuesta esperada");
        }
        return correct.trim().equalsIgnoreCase(answers.get(0).trim());
    }
}
