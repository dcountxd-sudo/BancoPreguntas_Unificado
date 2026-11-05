package co.unicauca.taller08.facade;

import co.unicauca.taller08.domain.Question;

public class QuestionPublisher {
    private final QuestionValidator validator;

    public QuestionPublisher(QuestionValidator validator) {
        this.validator = validator;
    }

    public void publish(Question question) {
        validator.validate(question);
        question.publish();
    }
}
