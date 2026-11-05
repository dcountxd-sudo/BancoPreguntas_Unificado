package co.unicauca.taller08.service;
import co.unicauca.taller08.builder.QuestionBuilder;
import co.unicauca.taller08.director.QuestionDirector;
import co.unicauca.taller08.domain.*;
import co.unicauca.taller08.repository.QuestionRepository;
import java.util.*;
/** Servicio de aplicación: crea, consulta y coordina las preguntas. */
public class QuestionService  {
    private final QuestionRepository repository;
    private final QuestionValidator validator;
    public QuestionService(QuestionRepository r,  QuestionValidator v) {
        repository=r;
        validator=v;
    }
    public Question create(QuestionBuilder b, String statement, String context, DifficultyLevel difficulty, String category, String subtopic, String competence, String author, String justification, String source, List<Option> options, String correct) {
        Question q=new QuestionDirector(b).make(statement, context, difficulty, category, subtopic, competence, author, justification, source, options, correct);
        validator.validate(q);
        repository.save(q);
        return q;
    }
    public void registerImported(Question q) {
        validator.validate(q);
        repository.save(q);
    }
    public List<Question> findAll() {
        return repository.findAll();
    }
    public Optional<Question> findByIndex(int i) {
        return repository.findByIndex(i);
    }
}
