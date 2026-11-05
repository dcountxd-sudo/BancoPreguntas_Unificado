
package co.unicauca.taller08.builder;

import co.unicauca.taller08.domain.DifficultyLevel;
import co.unicauca.taller08.domain.Option;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SingleChoiceQuestionBuilderTest {

    private SingleChoiceQuestionBuilder createValidBuilder() {
        SingleChoiceQuestionBuilder builder =
                new SingleChoiceQuestionBuilder();

        builder.buildStatement("¿Qué patrón crea objetos?");
        builder.buildContext("Pregunta sobre patrones de diseño");
        builder.buildDifficulty(DifficultyLevel.FACIL);
        builder.buildCategory("Software");
        builder.buildSubtopic("Patrones creacionales");
        builder.buildCompetence("Diseño de software");
        builder.buildAuthor("Autor");
        builder.buildJustification("Builder permite construir objetos");
        builder.buildSource("Material de clase");

        List<Option> options = Arrays.asList(
                new Option("A", "Builder"),
                new Option("B", "Adapter")
        );

        builder.buildAnswers(options, "A");

        return builder;
    }

    @Test
    void buildsSingleChoiceQuestionSuccessfully() {
        Question question = createValidBuilder().build();

        assertEquals(QuestionType.SINGLE_CHOICE, question.getType());
        assertEquals("A", question.getCorrectAnswer());
        assertEquals(2, question.getOptions().size());
    }

    @Test
    void rejectsMultipleCorrectAnswers() {
        SingleChoiceQuestionBuilder builder = createValidBuilder();
        builder.buildAnswers(
                Arrays.asList(
                        new Option("A", "Builder"),
                        new Option("B", "Adapter"),
                        new Option("C", "State")
                ),
                "A,C"
        );

        assertThrows(
                IllegalStateException.class,
                builder::build
        );
    }

    @Test
    void rejectsCorrectAnswerThatDoesNotExist() {
        SingleChoiceQuestionBuilder builder = createValidBuilder();
        builder.buildAnswers(
                Arrays.asList(
                        new Option("A", "Builder"),
                        new Option("B", "Adapter")
                ),
                "C"
        );

        assertThrows(
                IllegalStateException.class,
                builder::build
        );
    }

    @Test
    void rejectsDuplicateOptionLabels() {
        SingleChoiceQuestionBuilder builder = createValidBuilder();
        builder.buildAnswers(
                Arrays.asList(
                        new Option("A", "Builder"),
                        new Option("a", "Adapter")
                ),
                "A"
        );

        assertThrows(
                IllegalStateException.class,
                builder::build
        );
    }
}
