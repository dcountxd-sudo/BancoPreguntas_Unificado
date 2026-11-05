package co.unicauca.taller08.decorator;

import co.unicauca.taller08.domain.Question;

public class BasicQuestion implements QuestionComponent {
    private final Question question;

    public BasicQuestion(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("La pregunta es obligatoria.");
        }
        this.question = question;
    }

    @Override
    public String getDescription() {
        return question.getStatement();
    }

    @Override
    public double getScore() {
        return 1.0;
    }
}
