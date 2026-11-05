package co.unicauca.taller08.adapter;

import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExternalQuestionAdapterTest {

    @Test
    void adapterIsAQuestionProviderThatTransformsExternalJson() {
        QuestionProvider provider = new ExternalQuestionAdapter(new ExternalQuestionService());

        Question question = provider.getQuestion();

        assertEquals(QuestionType.OPEN, question.getType());
        assertEquals("¿Cuál es la capital de Colombia?", question.getStatement());
        assertEquals("Bogotá", question.getCorrectAnswer());
    }

    @Test
    void missingFieldInExternalJsonIsRejected() {
        ExternalQuestionService broken = new ExternalQuestionService() {
            @Override
            public String getQuestion() {
                return "{ \"statement\": \"Solo enunciado\" }";
            }
        };
        assertThrows(IllegalArgumentException.class,
                () -> new ExternalQuestionAdapter(broken).getQuestion());
    }

    @Test
    void nullServiceIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new ExternalQuestionAdapter(null));
    }
}
