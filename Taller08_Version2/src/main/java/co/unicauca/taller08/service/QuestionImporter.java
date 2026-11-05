package co.unicauca.taller08.service;

import co.unicauca.taller08.adapter.*;
import co.unicauca.taller08.domain.Question;

/** Subsistema de importación: obtiene preguntas de cualquier QuestionProvider (Adapter). */
public class QuestionImporter {

    private final ExternalQuestionMapper mapper = new ExternalQuestionMapper();

    /** Importa una pregunta desde cualquier fuente que cumpla la interfaz esperada. */
    public Question importFrom(QuestionProvider provider) {
        if (provider == null) throw new IllegalArgumentException("El proveedor es obligatorio");
        return provider.getQuestion();
    }

    /** Importa desde un DTO ya estructurado. */
    public Question importQuestion(ExternalQuestionDTO dto) {
        return mapper.adapt(dto);
    }
}
