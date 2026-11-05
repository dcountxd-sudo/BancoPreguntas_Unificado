package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

/**
 * Interfaz State del ciclo de vida de una pregunta (Figura 1 de la guía).
 * Por defecto toda operación está prohibida; cada estado concreto
 * sobrescribe únicamente las transiciones que sí permite.
 */
public interface QuestionState {

    String getName();

    default void submitForReview(Question q) { deny("enviar a revisión"); }
    default void assignReviewer(Question q)  { deny("asignar revisor"); }
    default void approve(Question q)         { deny("aprobar"); }
    default void reject(Question q)          { deny("rechazar"); }
    default void returnToDraft(Question q)   { deny("volver a borrador"); }
    default void publish(Question q)         { deny("publicar"); }
    default void archive(Question q)         { deny("archivar"); }

    private void deny(String operation) {
        throw new IllegalStateException(
                "No se puede " + operation + " desde el estado " + getName());
    }
}
