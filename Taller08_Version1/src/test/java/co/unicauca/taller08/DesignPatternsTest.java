package co.unicauca.taller08;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.unicauca.taller08.adapter.ExternalQuestionAdapter;
import co.unicauca.taller08.adapter.SampleExternalQuestionService;
import co.unicauca.taller08.decorator.BasicQuestion;
import co.unicauca.taller08.decorator.ContextualizedQuestion;
import co.unicauca.taller08.decorator.HighDifficultyQuestion;
import co.unicauca.taller08.decorator.QuestionComponent;
import co.unicauca.taller08.domain.Difficultylevel;
import co.unicauca.taller08.domain.Option;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import co.unicauca.taller08.facade.QuestionBankFacade;
import co.unicauca.taller08.templatemethod.MultipleAnswerEvaluator;
import co.unicauca.taller08.templatemethod.OpenQuestionEvaluator;
import co.unicauca.taller08.templatemethod.SingleChoiceEvaluator;
import co.unicauca.taller08.templatemethod.TrueFalseEvaluator;
import java.util.List;
import org.junit.jupiter.api.Test;

class DesignPatternsTest {

    @Test
    void statePatternAllowsOnlyLifecycleTransitions() {
        Question question = singleChoiceQuestion();

        assertEquals("Borrador", question.getState().getName());
        assertThrows(IllegalStateException.class, question::publish);
        question.submitForReview();
        assertEquals("Pendiente de revisión", question.getState().getName());
        question.assignReviewer();
        question.approve();
        assertEquals("Aprobada", question.getState().getName());
        question.publish();
        assertTrue(question.isAvailableForAssessment());
        assertThrows(IllegalStateException.class, question::submitForReview);
        question.archive();
        assertFalse(question.isAvailableForAssessment());
    }

    @Test
    void templateMethodEvaluatesSingleMultipleAndTrueFalseAnswers() {
        Question single = singleChoiceQuestion();
        Question multiple = new Question();
        multiple.setStatement("Selecciona los números pares");
        multiple.setType(QuestionType.MULTIPLE_ANSWER);
        multiple.setOptions(List.of(
                new Option("2", true),
                new Option("3", false),
                new Option("4", true)
        ));
        Question trueFalse = new Question();
        trueFalse.setStatement("La Tierra es redonda");
        trueFalse.setType(QuestionType.TRUE_FALSE);
        trueFalse.setOptions(List.of(new Option("Verdadero", true), new Option("Falso", false)));

        assertTrue(new SingleChoiceEvaluator().evaluate(single, List.of("Bogotá")));
        assertFalse(new SingleChoiceEvaluator().evaluate(single, List.of("Cali")));
        assertTrue(new MultipleAnswerEvaluator().evaluate(multiple, List.of("2", "4")));
        assertFalse(new MultipleAnswerEvaluator().evaluate(multiple, List.of("2")));
        assertTrue(new TrueFalseEvaluator().evaluate(trueFalse, List.of("Verdadero")));
        assertThrows(IllegalArgumentException.class,
                () -> new TrueFalseEvaluator().evaluate(trueFalse, List.of("Verdadero", "Falso")));
    }

    @Test
    void decoratorAddsCharacteristicsWithoutChangingQuestion() {
        Question question = singleChoiceQuestion();
        QuestionComponent decorated = new ContextualizedQuestion(
                new HighDifficultyQuestion(new BasicQuestion(question)));

        assertTrue(decorated.getDescription().contains("[ALTA DIFICULTAD]"));
        assertTrue(decorated.getDescription().contains("[CONTEXTUALIZADA]"));
        assertEquals(1.5, decorated.getScore());
    }

    @Test
    void adapterTransformsExternalQuestionIntoBankQuestion() {
        Question imported = new ExternalQuestionAdapter(new SampleExternalQuestionService()).getQuestion();

        assertEquals("¿Cuál es la capital de Colombia?", imported.getStatement());
        assertEquals("Bogotá", imported.getCorrectAnswer());
        assertEquals(QuestionType.OPEN, imported.getType());
        assertTrue(new OpenQuestionEvaluator().evaluate(imported, List.of("bogota")));
        assertFalse(new OpenQuestionEvaluator().evaluate(imported, List.of("Cali")));
    }

    @Test
    void facadeCoordinatesCreationLifecycleEvaluationAndImport() {
        QuestionBankFacade facade = new QuestionBankFacade();
        Question question = singleChoiceQuestion();
        String id = facade.createQuestion(question);

        assertThrows(IllegalStateException.class, () -> facade.evaluate(id, List.of("Bogotá")));
        facade.submitForReview(id);
        facade.assignReviewer(id);
        facade.approve(id);
        facade.publish(id);
        assertTrue(facade.evaluate(id, List.of("Bogotá")));
        assertEquals(1, facade.getQuestions().size());

        String importedId = facade.importQuestion(new ExternalQuestionAdapter(new SampleExternalQuestionService()));
        assertEquals("¿Cuál es la capital de Colombia?", facade.getQuestion(importedId).getStatement());
        facade.submitForReview(importedId);
        facade.assignReviewer(importedId);
        facade.approve(importedId);
        facade.publish(importedId);
        assertTrue(facade.evaluate(importedId, List.of("BOGOTA")));
    }

    private Question singleChoiceQuestion() {
        Question question = new Question();
        question.setStatement("¿Cuál es la capital de Colombia?");
        question.setType(QuestionType.MULTIPLE_CHOICE);
        question.setDifficultyLevel(Difficultylevel.MEDIUM);
        question.setOptions(List.of(
                new Option("Bogotá", true),
                new Option("Cali", false)
        ));
        return question;
    }
}
