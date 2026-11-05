package co.unicauca.taller08.facade;

import co.unicauca.taller08.domain.Question;

public class QuestionValidator {
    public void validate(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("La pregunta es obligatoria.");
        }
        question.validate();
    }
}
