
package co.unicauca.taller08.decorator;

import co.unicauca.taller08.domain.Question;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionDecoratorTest {

    private Question createQuestion() {
        Question question = new Question();
        question.setStatement("¿Qué es el patrón Builder?");
        return question;
    }

    @Test
    void basicFeatureReturnsOriginalQuestionStatement() {
        Question question = createQuestion();

        QuestionFeature feature = new BasicQuestionFeature(question);

        assertEquals(
                "¿Qué es el patrón Builder?",
                feature.describe()
        );
        assertSame(question, feature.getQuestion());
    }

    @Test
    void basicFeatureRejectsNullQuestion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new BasicQuestionFeature(null)
        );
    }

    @Test
    void highDifficultyDecoratorAddsDescription() {
        Question question = createQuestion();

        QuestionFeature feature = new HighDifficultyDecorator(
                new BasicQuestionFeature(question)
        );

        assertTrue(feature.describe().contains("Dificultad alta"));
        assertSame(question, feature.getQuestion());
    }

    @Test
    void contextualizedDecoratorAddsDescription() {
        Question question = createQuestion();

        QuestionFeature feature = new ContextualizedDecorator(
                new BasicQuestionFeature(question)
        );

        assertTrue(feature.describe().contains("Contextualizada"));
        assertSame(question, feature.getQuestion());
    }

    @Test
    void reviewFlagDecoratorMarksQuestionForReview() {
        Question question = createQuestion();

        QuestionFeature feature = new ReviewFlagDecorator(
                new BasicQuestionFeature(question)
        );

        assertTrue(
                feature.describe().contains("MARCADA PARA REVISIÓN")
        );
        assertSame(question, feature.getQuestion());
    }

    @Test
    void decoratorsCanBeCombined() {
        Question question = createQuestion();

        QuestionFeature feature = new HighDifficultyDecorator(
                new ContextualizedDecorator(
                        new ReviewFlagDecorator(
                                new BasicQuestionFeature(question)
                        )
                )
        );

        String description = feature.describe();

        assertTrue(description.contains("Dificultad alta"));
        assertTrue(description.contains("Contextualizada"));
        assertTrue(description.contains("MARCADA PARA REVISIÓN"));
        assertSame(question, feature.getQuestion());
    }

    @Test
    void decoratorRejectsNullComponent() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new HighDifficultyDecorator(null)
        );
    }


    @Test
    void feedbackDecoratorAddsDescription() {
        Question question = createQuestion();

        QuestionFeature feature = new FeedbackDecorator(
                new BasicQuestionFeature(question)
        );

        assertTrue(feature.describe().contains("Con retroalimentación"));
        assertSame(question, feature.getQuestion());
    }

    @Test
    void competencyDecoratorAddsDescription() {
        Question question = createQuestion();

        QuestionFeature feature = new CompetencyDecorator(
                new BasicQuestionFeature(question)
        );

        assertTrue(feature.describe().contains("Competencia asociada"));
        assertSame(question, feature.getQuestion());
    }

    @Test
    void allDecoratorsPreservePreviousDescriptions() {
        Question question = createQuestion();

        QuestionFeature feature = new CompetencyDecorator(
                new FeedbackDecorator(
                        new HighDifficultyDecorator(
                                new ContextualizedDecorator(
                                        new ReviewFlagDecorator(
                                                new BasicQuestionFeature(question)
                                        )
                                )
                        )
                )
        );

        String description = feature.describe();

        assertTrue(description.contains("Competencia asociada"));
        assertTrue(description.contains("Con retroalimentación"));
        assertTrue(description.contains("Dificultad alta"));
        assertTrue(description.contains("Contextualizada"));
        assertTrue(description.contains("MARCADA PARA REVISIÓN"));
        assertSame(question, feature.getQuestion());
    }


}
