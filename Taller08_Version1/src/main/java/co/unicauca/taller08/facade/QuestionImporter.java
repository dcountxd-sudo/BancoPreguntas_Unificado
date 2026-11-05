package co.unicauca.taller08.facade;

import co.unicauca.taller08.adapter.QuestionProvider;
import co.unicauca.taller08.domain.Question;

public class QuestionImporter {
    private final QuestionValidator validator;

    public QuestionImporter(QuestionValidator validator) {
        this.validator = validator;
    }

    public Question importQuestion(QuestionProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("El proveedor de preguntas es obligatorio.");
        }
        Question question = provider.getQuestion();
        validator.validate(question);
        return question;
    }
}
