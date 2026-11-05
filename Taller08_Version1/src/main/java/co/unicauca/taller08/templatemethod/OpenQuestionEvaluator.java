package co.unicauca.taller08.templatemethod;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import java.util.List;

/** Evalúa respuestas abiertas comparando texto normalizado con la respuesta esperada. */
public class OpenQuestionEvaluator extends QuestionEvaluator {
    @Override
    protected void validateQuestionType(Question question, List<String> answers) {
        if (question.getType() != QuestionType.OPEN) {
            throw new IllegalArgumentException("El evaluador de preguntas abiertas no corresponde al tipo de pregunta.");
        }
        if (answers.size() != 1) {
            throw new IllegalArgumentException("Debe proporcionar una única respuesta escrita.");
        }
        if (question.getCorrectAnswer() == null || question.getCorrectAnswer().isBlank()) {
            throw new IllegalStateException("La pregunta abierta no tiene una respuesta esperada configurada.");
        }
    }

    @Override
    protected boolean compareAnswers(Question question, List<String> answers) {
        return normalize(answers.get(0)).equals(normalize(question.getCorrectAnswer()));
    }
}
