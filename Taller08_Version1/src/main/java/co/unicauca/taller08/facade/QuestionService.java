package co.unicauca.taller08.facade;

import co.unicauca.taller08.domain.Question;
import java.util.List;

public class QuestionService {
    private final QuestionRepository repository;
    private final QuestionValidator validator;

    public QuestionService(QuestionRepository repository, QuestionValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public String create(Question question) {
        validator.validate(question);
        return repository.save(question);
    }

    public Question get(String id) {
        return repository.findById(id);
    }

    public List<Question> getAll() {
        return repository.findAll();
    }
}
