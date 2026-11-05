package co.unicauca.taller08;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.unicauca.taller08.builder.MultipleChoiceQuestionBuilder;
import co.unicauca.taller08.builder.OpenQuestionBuilder;
import co.unicauca.taller08.builder.QuestionBuilder;
import co.unicauca.taller08.builder.TrueFalseQuestionBuilder;
import co.unicauca.taller08.director.QuestionDirector;
import co.unicauca.taller08.domain.Difficultylevel;
import co.unicauca.taller08.domain.Option;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import java.util.List;
import org.junit.jupiter.api.Test;

class QuestionBuilderConsoleTest {

    @Test
    void shouldBuildOpenQuestion() {
        QuestionDirector director = new QuestionDirector();
        QuestionBuilder builder = new OpenQuestionBuilder();

        director.setQuestionBuilder(builder);
        director.construirPregunta();

        Question question = director.getQuestion();
        assertNotNull(question);
        assertEquals(QuestionType.OPEN, question.getType());
        assertEquals(Difficultylevel.MEDIUM, question.getDifficultyLevel());
        assertEquals("¿Cuál es la capital de Colombia?", question.getStatement());
    }

    @Test
    void shouldBuildMultipleChoiceQuestion() {
        QuestionDirector director = new QuestionDirector();
        QuestionBuilder builder = new MultipleChoiceQuestionBuilder();

        director.setQuestionBuilder(builder);
        director.construirPregunta();

        Question question = director.getQuestion();
        assertNotNull(question);
        assertEquals(QuestionType.MULTIPLE_CHOICE, question.getType());
        assertEquals(4, question.getOptions().size());
    }

    @Test
    void shouldBuildTrueFalseQuestion() {
        QuestionDirector director = new QuestionDirector();
        QuestionBuilder builder = new TrueFalseQuestionBuilder();

        director.setQuestionBuilder(builder);
        director.construirPregunta();

        Question question = director.getQuestion();
        assertNotNull(question);
        assertEquals(QuestionType.TRUE_FALSE, question.getType());
        assertEquals(2, question.getOptions().size());
    }

    @Test
    void shouldBuildValidQuestion() {
        QuestionDirector director = new QuestionDirector();
        QuestionBuilder builder = new OpenQuestionBuilder("¿Cuánto es 2 + 2?", Difficultylevel.EASY);

        director.setQuestionBuilder(builder);
        assertDoesNotThrow(director::construirPregunta);

        Question question = director.getQuestion();
        assertNotNull(question);
        assertEquals("¿Cuánto es 2 + 2?", question.getStatement());
        assertEquals(QuestionType.OPEN, question.getType());
    }

    @Test
    void shouldRejectQuestionWithoutStatement() {
        QuestionBuilder builder = new OpenQuestionBuilder("", Difficultylevel.MEDIUM);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            builder.crearNuevaPregunta();
            builder.buildType();
            builder.buildDifficultyLevel();
            builder.buildStatement();
            builder.buildOptions();
            builder.getQuestion().validate();
        });

        assertTrue(exception.getMessage().contains("enunciado"));
    }

    @Test
    void shouldRequireOptionsForMultipleChoice() {
        MultipleChoiceQuestionBuilder builder = new MultipleChoiceQuestionBuilder(
                "¿Cuál es la respuesta correcta?",
                Difficultylevel.MEDIUM,
                List.of()
        );

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            builder.crearNuevaPregunta();
            builder.buildType();
            builder.buildDifficultyLevel();
            builder.buildStatement();
            builder.buildOptions();
            builder.getQuestion().validate();
        });

        assertTrue(exception.getMessage().contains("opciones"));
    }

    @Test
    void shouldValidateCorrectAnswerCorrespondsToExistingOption() {
        Question question = new Question();
        question.setType(QuestionType.MULTIPLE_CHOICE);
        question.setStatement("¿Cuál es la capital de Colombia?");
        question.setOptions(List.of(
                new Option("Bogotá", true),
                new Option("Lima", false),
                new Option("Quito", false)
        ));

        assertTrue(question.isValidAnswer("Bogotá"));
        assertFalse(question.isValidAnswer("Medellín"));
    }

    @Test
    void shouldConstructDifferentProductsWithSameDirector() {
        QuestionDirector director = new QuestionDirector();

        director.setQuestionBuilder(new OpenQuestionBuilder());
        director.construirPregunta();
        Question open = director.getQuestion();

        director.setQuestionBuilder(new MultipleChoiceQuestionBuilder());
        director.construirPregunta();
        Question multiple = director.getQuestion();

        director.setQuestionBuilder(new TrueFalseQuestionBuilder());
        director.construirPregunta();
        Question trueFalse = director.getQuestion();

        assertEquals(QuestionType.OPEN, open.getType());
        assertEquals(QuestionType.MULTIPLE_CHOICE, multiple.getType());
        assertEquals(QuestionType.TRUE_FALSE, trueFalse.getType());
    }
}
