package co.unicauca.taller08.builder;
import co.unicauca.taller08.domain.DifficultyLevel;
import co.unicauca.taller08.domain.Option;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MultipleChoiceQuestionBuilderTest {
    private MultipleChoiceQuestionBuilder builder;
    private final List<Option> options = Arrays.asList(
            new Option("A", "Observer"),
            new Option("B", "Builder"),
            new Option("C", "Adapter"));

    @BeforeEach
    void setUp() {
        builder = new MultipleChoiceQuestionBuilder();
    }

    private void fillCommon() {
        builder.buildContext("Contexto sobre patrones de diseno");
        builder.buildStatement("Cual es un patron creacional?");
        builder.buildDifficulty(DifficultyLevel.FACIL);
        builder.buildCategory("Ingenieria de Software");
        builder.buildSubtopic("Patrones creacionales");
        builder.buildCompetence("Diseno de software");
        builder.buildAuthor("Nata");
        builder.buildJustification("Builder permite construir objetos paso a paso.");
        builder.buildSource("Referencia sobre patrones de diseno.");
    }

    @Test
    void construyeUnaPreguntaValida() {
        fillCommon();
        builder.buildAnswers(options, "B");

        Question q = builder.build();

        assertEquals(QuestionType.MULTIPLE_CHOICE, q.getType());
        assertEquals("Cual es un patron creacional?", q.getStatement());
        assertEquals(3, q.getOptions().size());
        assertEquals("B", q.getCorrectAnswer());
    }

    @Test
    void noPermiteConstruirSinEnunciado() {
        builder.buildContext("Contexto sobre patrones de diseno");
        builder.buildDifficulty(DifficultyLevel.FACIL);
        builder.buildCategory("Ingenieria de Software");
        builder.buildSubtopic("Patrones creacionales");
        builder.buildCompetence("Diseno de software");
        builder.buildAuthor("Nata");
        builder.buildJustification("Builder permite construir objetos paso a paso.");
        builder.buildSource("Referencia sobre patrones de diseno.");
        builder.buildAnswers(options, "B");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noPermiteEnunciadoEnBlanco() {
        fillCommon();
        builder.buildStatement("   ");
        builder.buildAnswers(options, "B");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinDificultad() {
        builder.buildContext("Contexto sobre patrones de diseno");
        builder.buildStatement("Enunciado");
        builder.buildCategory("Cat");
        builder.buildSubtopic("Subtema");
        builder.buildCompetence("Competencia");
        builder.buildAuthor("Nata");
        builder.buildJustification("Justificacion");
        builder.buildSource("Fuente");
        builder.buildAnswers(options, "B");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void debeTenerOpciones() {
        fillCommon();
        builder.buildAnswers(null, "A");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void debeTenerAlMenosDosOpciones() {
        fillCommon();
        builder.buildAnswers(
                Arrays.asList(new Option("A", "Unica")),
                "A");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noPermiteEtiquetasRepetidas() {
        fillCommon();
        builder.buildAnswers(
                Arrays.asList(
                        new Option("A", "x"),
                        new Option("a", "y")),
                "A");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void respuestaCorrectaDebeSerUnaOpcionExistente() {
        fillCommon();
        builder.buildAnswers(options, "Z");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void respuestaCorrectaNoPuedeEstarVacia() {
        fillCommon();
        builder.buildAnswers(options, null);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void despuesDeBuildElBuilderQuedaLimpio() {
        fillCommon();
        builder.buildAnswers(options, "B");
        builder.build();

        assertThrows(IllegalStateException.class, () -> builder.build());
    }
}

