package co.unicauca.taller08.templatemethod;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/** Define el algoritmo común de evaluación mediante Template Method. */
public abstract class QuestionEvaluator {
    /** Mantiene compatibilidad con los clientes que solo necesitan verdadero/falso. */
    public final boolean evaluate(Question question, List<String> answers) {
        return evaluateWithFeedback(question, answers).correct();
    }

    /**
     * Algoritmo fijo: 1) validar pregunta, 2) obtener/validar respuestas,
     * 3) comparar, 4) determinar resultado, 5) generar retroalimentación.
     */
    public final EvaluationResult evaluateWithFeedback(Question question, List<String> answers) {
        validateQuestion(question);                 // Paso 1: validar pregunta
        List<String> receivedAnswers = obtainAnswers(question, answers); // Paso 2
        boolean comparison = compareAnswers(question, receivedAnswers);   // Paso 3
        boolean result = determineResult(comparison);                     // Paso 4
        String feedback = generateFeedback(question, result);             // Paso 5
        return new EvaluationResult(result, feedback);
    }

    protected void validateQuestion(Question question) {
        if (question == null) throw new IllegalArgumentException("La pregunta es obligatoria.");
        question.validate();
    }

    protected List<String> obtainAnswers(Question question, List<String> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar al menos una respuesta.");
        }
        Set<String> unique = new HashSet<>();
        for (String answer : answers) {
            if (answer == null || answer.isBlank()) {
                throw new IllegalArgumentException("La respuesta no puede estar vacía.");
            }
            if (question.getType() != QuestionType.OPEN && !question.isValidAnswer(answer)) {
                throw new IllegalArgumentException("La respuesta seleccionada no existe entre las opciones.");
            }
            if (!unique.add(normalize(answer))) {
                throw new IllegalArgumentException("No se permiten respuestas duplicadas.");
            }
        }
        validateQuestionType(question, answers);
        return List.copyOf(answers);
    }

    protected abstract void validateQuestionType(Question question, List<String> answers);
    protected abstract boolean compareAnswers(Question question, List<String> answers);

    protected boolean determineResult(boolean comparison) {
        return comparison;
    }

    protected String generateFeedback(Question question, boolean correct) {
        if (correct) return question.getExplanation().isBlank()
                ? "Respuesta correcta." : "Respuesta correcta. " + question.getExplanation();
        return question.getExplanation().isBlank()
                ? "Respuesta incorrecta. Revise el contenido e inténtelo de nuevo."
                : "Respuesta incorrecta. " + question.getExplanation();
    }

    protected Set<String> correctAnswers(Question question) {
        return question.getOptions().stream().filter(o -> o.isCorrect())
                .map(o -> normalize(o.getText())).collect(Collectors.toSet());
    }

    protected Set<String> selectedAnswers(List<String> answers) {
        return answers.stream().map(QuestionEvaluator::normalize).collect(Collectors.toSet());
    }

    /** Normaliza espacios, mayúsculas y tildes para comparar respuestas escritas. */
    protected static String normalize(String value) {
        return Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }
}
