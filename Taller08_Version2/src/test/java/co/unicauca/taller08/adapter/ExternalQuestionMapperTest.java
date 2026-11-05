
package co.unicauca.taller08.adapter;

import co.unicauca.taller08.domain.DifficultyLevel;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExternalQuestionMapperTest {

    private final ExternalQuestionMapper adapter = new ExternalQuestionMapper();

    @Test
    void adaptsTrueFalseQuestion() {
        ExternalQuestionDTO dto = new ExternalQuestionDTO(
                "verdadero_falso",
                "El cielo es azul.",
                "Verdadero"
        );

        Question question = adapter.adapt(dto);

        assertEquals(QuestionType.TRUE_FALSE, question.getType());
        assertEquals("El cielo es azul.", question.getStatement());
        assertEquals("V", question.getCorrectAnswer());
    }

    @Test
    void adaptsOpenQuestion() {
        ExternalQuestionDTO dto = new ExternalQuestionDTO(
                "abierta",
                "¿Qué es Builder?",
                "Patrón de diseño"
        );

        Question question = adapter.adapt(dto);

        assertEquals(QuestionType.OPEN, question.getType());
        assertEquals("¿Qué es Builder?", question.getStatement());
        assertEquals("Patrón de diseño", question.getCorrectAnswer());
    }

    @Test
    void unknownTypeUsesMultipleChoiceBuilder() {
        ExternalQuestionDTO dto = new ExternalQuestionDTO(
                "tipo_desconocido",
                "¿Cuál es la respuesta?",
                "A"
        );

        dto.opciones.add(new ExternalQuestionDTO.ExternalOption("A", "Opción A"));
        dto.opciones.add(new ExternalQuestionDTO.ExternalOption("B", "Opción B"));

        Question question = adapter.adapt(dto);

        assertEquals(QuestionType.MULTIPLE_CHOICE, question.getType());
        assertEquals(2, question.getOptions().size());
    }

    @Test
    void missingDataUsesDefaultValues() {
        ExternalQuestionDTO dto = new ExternalQuestionDTO(
                "abierta",
                " ",
                "Respuesta esperada"
        );

        dto.dificultad = "valor_invalido";

        Question question = adapter.adapt(dto);

        assertEquals("Pregunta importada", question.getStatement());
        assertEquals(DifficultyLevel.MEDIO, question.getDifficulty());
        assertEquals("Importadas", question.getCategory());
        assertEquals("Sistema externo", question.getAuthor());
    }

    @Test
    void nullDtoIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> adapter.adapt(null)
        );
    }

    @Test
    void trueFalseEnglishAnswerIsNormalized() {
        ExternalQuestionDTO dto = new ExternalQuestionDTO(
                "true_false",
                "The statement is true.",
                "false"
        );

        Question question = adapter.adapt(dto);

        assertEquals(QuestionType.TRUE_FALSE, question.getType());
        assertEquals("F", question.getCorrectAnswer());
    }
}
