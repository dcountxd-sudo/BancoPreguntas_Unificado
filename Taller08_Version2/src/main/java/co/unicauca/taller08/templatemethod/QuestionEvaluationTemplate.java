package co.unicauca.taller08.templatemethod;

import co.unicauca.taller08.domain.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * TEMPLATE METHOD: algoritmo general de evaluación de respuestas (Figura del taller).
 *
 * Pasos comunes (se implementan aquí): verificar pregunta publicada, verificar
 * que haya respuesta y generar la retroalimentación.
 * Pasos que cambian según el tipo de pregunta (abstractos): validar el formato de
 * la respuesta y compararla con las respuestas correctas.
 */
public abstract class QuestionEvaluationTemplate {

    /** Método plantilla: ninguna subclase puede alterar el orden de los pasos. */
    public final EvaluationResult evaluate(Question question, List<String> answers) {
        checkEvaluable(question);                         // paso común
        validate(question, answers);                      // 1. validar respuesta (varía)
        boolean correct = compareAnswers(question, answers); // 2-3. comparar con las correctas (varía)
        return new EvaluationResult(correct, buildFeedback(question, correct)); // 4-5. resultado + retroalimentación
    }

    /** Conveniencia: recibe la respuesta como texto tal como la escribe el estudiante. */
    public final EvaluationResult evaluate(Question question, String answer) {
        if (answer == null || answer.trim().isEmpty()) {
            throw new IllegalArgumentException("La respuesta es obligatoria");
        }
        return evaluate(question, parseAnswers(answer));
    }

    /** Paso con implementación por defecto: separa las respuestas por coma. Puede sobrescribirse. */
    protected List<String> parseAnswers(String answer) {
        List<String> answers = new ArrayList<>();
        for (String part : answer.split(",", -1)) {
            answers.add(part.trim());
        }
        return answers;
    }

    /** Paso que cambia: verifica que la respuesta tenga un formato válido para el tipo de pregunta. */
    protected abstract void validate(Question question, List<String> answers);

    /** Paso que cambia: determina si las respuestas del estudiante coinciden con las correctas. */
    protected abstract boolean compareAnswers(Question question, List<String> answers);

    /** Hook: retroalimentación del resultado; las subclases pueden enriquecerla. */
    protected String buildFeedback(Question question, boolean correct) {
        return correct ? "Respuesta correcta" : "Respuesta incorrecta";
    }

    private void checkEvaluable(Question question) {
        if (question == null) throw new IllegalArgumentException("La pregunta es obligatoria");
        if (!"PUBLICADA".equals(question.getStatus())) {
            throw new IllegalStateException("Solo se pueden evaluar preguntas publicadas");
        }
    }
}
