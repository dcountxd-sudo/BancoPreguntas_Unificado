package co.unicauca.taller08.director;

import co.unicauca.taller08.builder.MultipleChoiceQuestionBuilder;
import co.unicauca.taller08.builder.OpenQuestionBuilder;
import co.unicauca.taller08.builder.TrueFalseQuestionBuilder;
import co.unicauca.taller08.domain.DifficultyLevel;
import co.unicauca.taller08.domain.Option;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class QuestionDirectorTest {

    @Test
    void elMismoProcesoConstruyeDiferentesTiposDePregunta() {

        QuestionDirector director =
                new QuestionDirector(new MultipleChoiceQuestionBuilder());

        Question mc = director.make(
                "Enunciado MC",
                "Contexto MC",
                DifficultyLevel.FACIL,
                "Cat",
                "Subtema MC",
                "Competencia MC",
                "Nata",
                "Justificacion MC",
                "Fuente MC",
                Arrays.asList(
                        new Option("A", "uno"),
                        new Option("B", "dos")
                ),
                "A"
        );

        director.changeBuilder(new TrueFalseQuestionBuilder());

        Question tf = director.make(
                "Enunciado VF",
                "Contexto VF",
                DifficultyLevel.MEDIO,
                "Cat",
                "Subtema VF",
                "Competencia VF",
                "Nata",
                "Justificacion VF",
                "Fuente VF",
                null,
                "V"
        );

        director.changeBuilder(new OpenQuestionBuilder());

        Question open = director.make(
                "Enunciado abierta",
                "Contexto abierta",
                DifficultyLevel.DIFICIL,
                "Cat",
                "Subtema abierta",
                "Competencia abierta",
                "Nata",
                "Justificacion abierta",
                "Fuente abierta",
                null,
                "Respuesta modelo"
        );

        assertEquals(QuestionType.MULTIPLE_CHOICE, mc.getType());
        assertEquals(QuestionType.TRUE_FALSE, tf.getType());
        assertEquals(QuestionType.OPEN, open.getType());
    }

    @Test
    void elDirectorPropagaLasReglasDeNegocioDelBuilder() {

        QuestionDirector director =
                new QuestionDirector(new MultipleChoiceQuestionBuilder());

        assertThrows(
                IllegalStateException.class,
                () -> director.make(
                        null,
                        "Contexto",
                        DifficultyLevel.FACIL,
                        "Cat",
                        "Subtema",
                        "Competencia",
                        "Nata",
                        "Justificacion",
                        "Fuente",
                        Arrays.asList(
                                new Option("A", "uno"),
                                new Option("B", "dos")
                        ),
                        "A"
                )
        );
    }

    @Test
    void elMismoBuilderSePuedeReutilizar() {

        QuestionDirector director =
                new QuestionDirector(new OpenQuestionBuilder());

        Question q1 = director.make(
                "Uno",
                "Contexto 1",
                DifficultyLevel.FACIL,
                "Cat",
                "Subtema 1",
                "Competencia 1",
                "Nata",
                "Justificacion 1",
                "Fuente 1",
                null,
                "R1"
        );

        Question q2 = director.make(
                "Dos",
                "Contexto 2",
                DifficultyLevel.FACIL,
                "Cat",
                "Subtema 2",
                "Competencia 2",
                "Nata",
                "Justificacion 2",
                "Fuente 2",
                null,
                "R2"
        );

        assertNotSame(q1, q2);

        assertEquals("Uno", q1.getStatement());
        assertEquals("Dos", q2.getStatement());
    }
}