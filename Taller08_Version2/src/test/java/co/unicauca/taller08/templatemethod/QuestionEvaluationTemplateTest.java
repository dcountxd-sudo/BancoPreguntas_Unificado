
package co.unicauca.taller08.templatemethod;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionEvaluationTemplateTest {

    private Question createQuestion(
            QuestionType type, String correctAnswer) {

        Question question = new Question();
        question.setType(type);
        question.setCorrectAnswer(correctAnswer);
        question.setStatus("PUBLICADA");

        return question;
    }

    @Test
    void singleChoiceAcceptsCorrectAnswer() {
        Question question = createQuestion(
                QuestionType.SINGLE_CHOICE, "B");

        SingleChoiceEvaluator evaluator =
                new SingleChoiceEvaluator();

        assertTrue(evaluator.evaluate(question, "b").isCorrect());
    }

    @Test
    void singleChoiceRejectsIncorrectAnswer() {
        Question question = createQuestion(
                QuestionType.SINGLE_CHOICE, "B");

        SingleChoiceEvaluator evaluator =
                new SingleChoiceEvaluator();

        assertFalse(evaluator.evaluate(question, "A").isCorrect());
    }

    @Test
    void multipleChoiceAcceptsAnswersInDifferentOrder() {
        Question question = createQuestion(
                QuestionType.MULTIPLE_CHOICE, "A,C");

        MultipleChoiceEvaluator evaluator =
                new MultipleChoiceEvaluator();

        assertTrue(evaluator.evaluate(question, "C,A").isCorrect());
    }

    @Test
    void multipleChoiceRejectsIncompleteAnswer() {
        Question question = createQuestion(
                QuestionType.MULTIPLE_CHOICE, "A,C");

        MultipleChoiceEvaluator evaluator =
                new MultipleChoiceEvaluator();

        assertFalse(evaluator.evaluate(question, "A").isCorrect());
    }

    @Test
    void trueFalseAcceptsEquivalentAnswer() {
        Question question = createQuestion(
                QuestionType.TRUE_FALSE, "V");

        TrueFalseEvaluator evaluator =
                new TrueFalseEvaluator();

        assertTrue(
                evaluator.evaluate(question, "verdadero").isCorrect());
    }

    @Test
    void trueFalseRejectsInvalidAnswer() {
        Question question = createQuestion(
                QuestionType.TRUE_FALSE, "V");

        TrueFalseEvaluator evaluator =
                new TrueFalseEvaluator();

        assertThrows(
                IllegalArgumentException.class,
                () -> evaluator.evaluate(question, "quizás"));
    }

    @Test
    void openQuestionAcceptsExpectedAnswerIgnoringCase() {
        Question question = createQuestion(
                QuestionType.OPEN, "Patrón Builder");

        OpenQuestionEvaluator evaluator =
                new OpenQuestionEvaluator();

        assertTrue(
                evaluator.evaluate(question, "patrón builder").isCorrect());
    }

    @Test
    void cannotEvaluateUnpublishedQuestion() {
        Question question = createQuestion(
                QuestionType.SINGLE_CHOICE, "B");

        question.setStatus("BORRADOR");

        SingleChoiceEvaluator evaluator =
                new SingleChoiceEvaluator();

        assertThrows(
                IllegalStateException.class,
                () -> evaluator.evaluate(question, "B"));
    }

    @Test
    void singleChoiceRejectsMultipleSelectedAnswers() {
        Question question = createQuestion(QuestionType.SINGLE_CHOICE, "B");
        assertThrows(IllegalArgumentException.class,
                () -> new SingleChoiceEvaluator().evaluate(question, "A,B"));
    }

    @Test
    void evaluatorRejectsQuestionOfAnotherType() {
        Question question = createQuestion(QuestionType.TRUE_FALSE, "V");
        assertThrows(IllegalArgumentException.class,
                () -> new MultipleChoiceEvaluator().evaluate(question, "A"));
    }

    @Test
    void feedbackIsGeneratedByTheTemplate() {
        Question question = createQuestion(QuestionType.SINGLE_CHOICE, "B");
        assertEquals("Respuesta correcta",
                new SingleChoiceEvaluator().evaluate(question, "B").getMessage());
        assertEquals("Respuesta incorrecta",
                new SingleChoiceEvaluator().evaluate(question, "A").getMessage());
    }

    @Test
    void openAnswerMayContainCommas() {
        Question question = createQuestion(QuestionType.OPEN, "uno, dos y tres");
        assertTrue(new OpenQuestionEvaluator().evaluate(question, "Uno, Dos y Tres").isCorrect());
    }
}
