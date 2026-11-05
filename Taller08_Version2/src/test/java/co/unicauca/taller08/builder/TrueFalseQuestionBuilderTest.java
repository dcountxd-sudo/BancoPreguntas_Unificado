package co.unicauca.taller08.builder;

import co.unicauca.taller08.domain.DifficultyLevel;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class TrueFalseQuestionBuilderTest {

    private TrueFalseQuestionBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new TrueFalseQuestionBuilder();
    }

    private void fillCommon() {
        builder.buildContext("Contexto sobre patrones de diseno");
        builder.buildStatement("Builder es un patron creacional.");
        builder.buildDifficulty(DifficultyLevel.FACIL);
        builder.buildCategory("Ingenieria de Software");
        builder.buildSubtopic("Patrones creacionales");
        builder.buildCompetence("Diseno de software");
        builder.buildAuthor("Nata");
        builder.buildJustification(
                "Builder permite construir objetos paso a paso.");
        builder.buildSource("Referencia sobre patrones de diseno.");
    }

    @Test
    void construyeUnaPreguntaVerdaderoFalsoValida() {
        fillCommon();

        builder.buildAnswers(
                Collections.emptyList(),
                "V");

        Question q = builder.build();

        assertEquals(QuestionType.TRUE_FALSE, q.getType());
        assertEquals(
                "Builder es un patron creacional.",
                q.getStatement());
        assertEquals("V", q.getCorrectAnswer());
        assertEquals(2, q.getOptions().size());
    }

    @Test
    void aceptaRespuestaVerdadero() {
        fillCommon();

        builder.buildAnswers(
                Collections.emptyList(),
                "Verdadero");

        Question q = builder.build();

        assertEquals("V", q.getCorrectAnswer());
    }

    @Test
    void aceptaRespuestaFalso() {
        fillCommon();

        builder.buildAnswers(
                Collections.emptyList(),
                "Falso");

        Question q = builder.build();

        assertEquals("F", q.getCorrectAnswer());
    }

    @Test
    void noPermiteConstruirSinContexto() {
        fillCommon();
        builder.buildContext(null);

        builder.buildAnswers(
                Collections.emptyList(),
                "V");

        assertThrows(
                IllegalStateException.class,
                () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinEnunciado() {
        fillCommon();
        builder.buildStatement(null);

        builder.buildAnswers(
                Collections.emptyList(),
                "V");

        assertThrows(
                IllegalStateException.class,
                () -> builder.build());
    }

    @Test
    void noPermiteEnunciadoEnBlanco() {
        fillCommon();
        builder.buildStatement("   ");

        builder.buildAnswers(
                Collections.emptyList(),
                "V");

        assertThrows(
                IllegalStateException.class,
                () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinDificultad() {
        fillCommon();
        builder.buildDifficulty(null);

        builder.buildAnswers(
                Collections.emptyList(),
                "V");

        assertThrows(
                IllegalStateException.class,
                () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinCategoria() {
        fillCommon();
        builder.buildCategory(null);

        builder.buildAnswers(
                Collections.emptyList(),
                "V");

        assertThrows(
                IllegalStateException.class,
                () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinSubtema() {
        fillCommon();
        builder.buildSubtopic(null);

        builder.buildAnswers(
                Collections.emptyList(),
                "V");

        assertThrows(
                IllegalStateException.class,
                () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinCompetencia() {
        fillCommon();
        builder.buildCompetence(null);

        builder.buildAnswers(
                Collections.emptyList(),
                "V");

        assertThrows(
                IllegalStateException.class,
                () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinAutor() {
        fillCommon();
        builder.buildAuthor(null);

        builder.buildAnswers(
                Collections.emptyList(),
                "V");

        assertThrows(
                IllegalStateException.class,
                () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinJustificacion() {
        fillCommon();
        builder.buildJustification(null);

        builder.buildAnswers(
                Collections.emptyList(),
                "V");

        assertThrows(
                IllegalStateException.class,
                () -> builder.build());
    }

    @Test
    void noPermiteConstruirSinFuente() {
        fillCommon();
        builder.buildSource(null);

        builder.buildAnswers(
                Collections.emptyList(),
                "V");

        assertThrows(
                IllegalStateException.class,
                () -> builder.build());
    }

    @Test
    void noPermiteRespuestaInvalida() {
        fillCommon();

        builder.buildAnswers(
                Collections.emptyList(),
                "X");

        assertThrows(
                IllegalStateException.class,
                () -> builder.build());
    }

    @Test
    void noPermiteRespuestaVacia() {
        fillCommon();

        builder.buildAnswers(
                Collections.emptyList(),
                null);

        assertThrows(
                IllegalStateException.class,
                () -> builder.build());
    }

    @Test
    void generaLasOpcionesVerdaderoYFalso() {
        fillCommon();

        builder.buildAnswers(
                Collections.emptyList(),
                "V");

        Question q = builder.build();

        assertEquals(2, q.getOptions().size());
        assertEquals("V", q.getOptions().get(0).getLabel());
        assertEquals("F", q.getOptions().get(1).getLabel());
    }

    @Test
    void ignoraLasOpcionesRecibidas() {
        fillCommon();

        builder.buildAnswers(
                Collections.emptyList(),
                "V");

        Question q = builder.build();

        assertEquals(2, q.getOptions().size());
        assertEquals("V", q.getOptions().get(0).getLabel());
        assertEquals("F", q.getOptions().get(1).getLabel());
    }

    @Test
    void despuesDeBuildElBuilderQuedaLimpio() {
        fillCommon();

        builder.buildAnswers(
                Collections.emptyList(),
                "V");

        builder.build();

        assertThrows(
                IllegalStateException.class,
                () -> builder.build());
    }
}

