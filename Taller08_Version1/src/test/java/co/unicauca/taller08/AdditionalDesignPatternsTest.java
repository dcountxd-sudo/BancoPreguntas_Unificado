package co.unicauca.taller08;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.unicauca.taller08.adapter.ExternalQuestionAdapter;
import co.unicauca.taller08.adapter.ExternalQuestionService;
import co.unicauca.taller08.decorator.QuestionComponent;
import co.unicauca.taller08.domain.Difficultylevel;
import co.unicauca.taller08.domain.Option;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import co.unicauca.taller08.facade.QuestionBankFacade;
import co.unicauca.taller08.facade.QuestionFeature;
import co.unicauca.taller08.templatemethod.EvaluationResult;
import co.unicauca.taller08.templatemethod.MultipleAnswerEvaluator;
import co.unicauca.taller08.templatemethod.OpenQuestionEvaluator;
import co.unicauca.taller08.templatemethod.SingleChoiceEvaluator;
import co.unicauca.taller08.templatemethod.TrueFalseEvaluator;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Pruebas adicionales de validación, errores y casos límite de los patrones del taller. */
class AdditionalDesignPatternsTest {

    @Test
    void openQuestionEvaluatorIgnoresCaseAccentsAndExtraSpaces() {
        Question question = openQuestion("La capital es Bogotá", "Bogotá");

        assertTrue(new OpenQuestionEvaluator().evaluate(question, List.of("  BOGOTA  ")));
        assertTrue(new OpenQuestionEvaluator().evaluate(question, List.of("bogotá")));
    }

    @Test
    void openQuestionEvaluatorRejectsIncorrectAnswer() {
        Question question = openQuestion("La capital es Bogotá", "Bogotá");

        assertFalse(new OpenQuestionEvaluator().evaluate(question, List.of("Medellín")));
    }

    @Test
    void evaluatorRejectsMissingQuestionAndMissingAnswer() {
        assertThrows(IllegalArgumentException.class,
                () -> new OpenQuestionEvaluator().evaluate(null, List.of("Bogotá")));
        assertThrows(IllegalArgumentException.class,
                () -> new OpenQuestionEvaluator().evaluate(openQuestion("Capital", "Bogotá"), List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> new OpenQuestionEvaluator().evaluate(openQuestion("Capital", "Bogotá"), null));
    }

    @Test
    void evaluatorRejectsBlankAndRepeatedAnswers() {
        Question question = multipleAnswerQuestion();
        MultipleAnswerEvaluator evaluator = new MultipleAnswerEvaluator();

        assertThrows(IllegalArgumentException.class, () -> evaluator.evaluate(question, List.of("2", " ")));
        assertThrows(IllegalArgumentException.class, () -> evaluator.evaluate(question, List.of("2", "2")));
    }

    @Test
    void evaluatorRejectsQuestionOfAnotherType() {
        Question question = openQuestion("Capital", "Bogotá");

        assertThrows(IllegalArgumentException.class,
                () -> new SingleChoiceEvaluator().evaluate(question, List.of("Bogotá")));
        assertThrows(IllegalArgumentException.class,
                () -> new TrueFalseEvaluator().evaluate(question, List.of("Verdadero")));
    }

    @Test
    void multipleAnswerRequiresTheCompleteSetOfCorrectOptions() {
        Question question = multipleAnswerQuestion();
        MultipleAnswerEvaluator evaluator = new MultipleAnswerEvaluator();

        assertTrue(evaluator.evaluate(question, List.of("2", "4")));
        assertFalse(evaluator.evaluate(question, List.of("2")));
        assertFalse(evaluator.evaluate(question, List.of("2", "3")));
    }

    @Test
    void evaluationResultIncludesFeedback() {
        Question question = openQuestion("Capital", "Bogotá");
        question.setExplanation("Es la capital de Colombia.");

        EvaluationResult result = new OpenQuestionEvaluator()
                .evaluateWithFeedback(question, List.of("Bogotá"));

        assertTrue(result.correct());
        assertTrue(result.feedback().contains("Respuesta correcta"));
        assertTrue(result.feedback().contains("capital de Colombia"));
    }

    @Test
    void adapterRejectsNullServiceAndEmptyJson() {
        assertThrows(IllegalArgumentException.class, () -> new ExternalQuestionAdapter(null));
        ExternalQuestionService emptyService = () -> "  ";
        assertThrows(IllegalStateException.class,
                () -> new ExternalQuestionAdapter(emptyService).getQuestion());
    }

    @Test
    void adapterRejectsJsonWithoutRequiredFields() {
        ExternalQuestionService missingAnswer = () -> "{\"question_text\":\"¿Capital?\"}";
        ExternalQuestionService missingQuestion = () -> "{\"answer_text\":\"Bogotá\"}";

        assertThrows(IllegalStateException.class,
                () -> new ExternalQuestionAdapter(missingAnswer).getQuestion());
        assertThrows(IllegalStateException.class,
                () -> new ExternalQuestionAdapter(missingQuestion).getQuestion());
    }

    @Test
    void adapterMapsJsonFieldsAndUnescapesQuotes() {
        ExternalQuestionService service = () ->
                "{\"question_text\":\"¿Quién dijo \\\"Eureka\\\"?\",\"answer_text\":\"Arquímedes\"}";

        Question question = new ExternalQuestionAdapter(service).getQuestion();

        assertEquals("¿Quién dijo \"Eureka\"?", question.getStatement());
        assertEquals("Arquímedes", question.getCorrectAnswer());
        assertEquals(QuestionType.OPEN, question.getType());
        assertEquals(Difficultylevel.MEDIUM, question.getDifficultyLevel());
    }

    @Test
    void questionValidationRejectsRepeatedOptionsAndNoCorrectAnswer() {
        Question repeated = new Question();
        repeated.setStatement("Elige una opción");
        repeated.setType(QuestionType.MULTIPLE_CHOICE);
        repeated.setOptions(List.of(new Option("Bogotá", true), new Option(" bogotá ", false)));
        assertThrows(IllegalStateException.class, repeated::validate);

        Question withoutCorrect = new Question();
        withoutCorrect.setStatement("Elige una opción");
        withoutCorrect.setType(QuestionType.MULTIPLE_CHOICE);
        withoutCorrect.setOptions(List.of(new Option("Bogotá", false), new Option("Cali", false)));
        assertThrows(IllegalStateException.class, withoutCorrect::validate);
    }

    @Test
    void facadeReturnsFeedbackAndCanDecorateQuestionWithSeveralFeatures() {
        QuestionBankFacade facade = new QuestionBankFacade();
        Question question = openQuestion("¿Capital de Colombia?", "Bogotá");
        question.setExplanation("Bogotá es la capital.");
        String id = facade.createQuestion(question);
        facade.submitForReview(id);
        facade.assignReviewer(id);
        facade.approve(id);
        facade.publish(id);

        EvaluationResult result = facade.evaluateWithFeedback(id, List.of("Bogotá"));
        QuestionComponent decorated = facade.decorateQuestion(id,
                QuestionFeature.HIGH_DIFFICULTY,
                QuestionFeature.CONTEXTUALIZED,
                QuestionFeature.FEEDBACK,
                QuestionFeature.COMPETENCY);

        assertTrue(result.correct());
        assertNotNull(decorated);
        assertTrue(decorated.getDescription().contains("ALTA DIFICULTAD"));
        assertTrue(decorated.getDescription().contains("CONTEXTUALIZADA"));
    }

    @Test
    void facadeRejectsEvaluationOfUnknownQuestion() {
        QuestionBankFacade facade = new QuestionBankFacade();
        assertThrows(IllegalArgumentException.class, () -> facade.evaluate("no-existe", List.of("A")));
    }

    private Question openQuestion(String statement, String correctAnswer) {
        Question question = new Question();
        question.setStatement(statement);
        question.setType(QuestionType.OPEN);
        question.setDifficultyLevel(Difficultylevel.MEDIUM);
        question.setCorrectAnswer(correctAnswer);
        return question;
    }

    private Question multipleAnswerQuestion() {
        Question question = new Question();
        question.setStatement("Selecciona los números pares");
        question.setType(QuestionType.MULTIPLE_ANSWER);
        question.setOptions(List.of(
                new Option("2", true),
                new Option("3", false),
                new Option("4", true)));
        return question;
    }
}
