package co.unicauca.taller08.builder;

import co.unicauca.taller08.domain.DifficultyLevel;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class OpenQuestionBuilderTest {

    private OpenQuestionBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new OpenQuestionBuilder();
    }

    private void fillCommon() {
        builder.buildContext("Contexto sobre patrones de diseno");
        builder.buildStatement("Explique que es el patron Builder.");
        builder.buildDifficulty(DifficultyLevel.FACIL);
        builder.buildCategory("Ingenieria de Software");
        builder.buildSubtopic("Patrones creacionales");
        builder.buildCompetence("Diseno de software");
        builder.buildAuthor("Nata");
        builder.buildJustification("Builder permite construir objetos paso a paso.");
        builder.buildSource("Referencia sobre patrones de diseno.");
        builder.buildAnswers(
                Collections.emptyList(),
                "Es un patron que permite construir objetos paso a paso.");
    }

    @Test
    void construyeUnaPreguntaValida() {
        fillCommon();

        Question q = builder.build();

        assertEquals(QuestionType.OPEN, q.getType());
        assertEquals(
                "Explique que es el patron Builder.",
                q.getStatement());
        assertEquals(
                "Es un patron que permite construir objetos paso a paso.",
                q.getCorrectAnswer());
        assertTrue(q.getOptions().isEmpty());
    }

    @Test
    void noPermiteConstruirSinContexto() {
        fillCommon();
        builder.buildContext(null);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinEnunciado() {
        fillCommon();
        builder.buildStatement(null);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noPermiteEnunciadoEnBlanco() {
        fillCommon();
        builder.buildStatement("   ");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinDificultad() {
        fillCommon();
        builder.buildDifficulty(null);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinCategoria() {
        fillCommon();
        builder.buildCategory(null);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinSubtema() {
        fillCommon();
        builder.buildSubtopic(null);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinCompetencia() {
        fillCommon();
        builder.buildCompetence(null);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinAutor() {
        fillCommon();
        builder.buildAuthor(null);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinJustificacion() {
        fillCommon();
        builder.buildJustification(null);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinFuente() {
        fillCommon();
        builder.buildSource(null);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinRespuesta() {
        fillCommon();
        builder.buildAnswers(
                Collections.emptyList(),
                null);

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noPermiteRespuestaEnBlanco() {
        fillCommon();
        builder.buildAnswers(
                Collections.emptyList(),
                "   ");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    void noNecesitaOpciones() {
        fillCommon();

        Question q = builder.build();

        assertTrue(q.getOptions().isEmpty());
        assertEquals(
                "Es un patron que permite construir objetos paso a paso.",
                q.getCorrectAnswer());
    }

    @Test
    void despuesDeBuildElBuilderQuedaLimpio() {
        fillCommon();

        builder.build();

        assertThrows(IllegalStateException.class, () -> builder.build());
    }
}

