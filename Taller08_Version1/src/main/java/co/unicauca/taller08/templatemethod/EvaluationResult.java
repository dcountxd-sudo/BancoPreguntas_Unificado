package co.unicauca.taller08.templatemethod;

/** Resultado de una evaluación, con veredicto y retroalimentación para el estudiante. */
public record EvaluationResult(boolean correct, String feedback) {
}
