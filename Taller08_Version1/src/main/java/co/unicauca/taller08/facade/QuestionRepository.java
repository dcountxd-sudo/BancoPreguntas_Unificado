package co.unicauca.taller08.facade;

import co.unicauca.taller08.domain.Question;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class QuestionRepository {
    private final Map<String, Question> questions = new LinkedHashMap<>();

    public String save(Question question) {
        String id = UUID.randomUUID().toString();
        questions.put(id, question);
        return id;
    }

    public Question findById(String id) {
        Question question = questions.get(id);
        if (question == null) {
            throw new IllegalArgumentException("No existe una pregunta con identificador " + id + ".");
        }
        return question;
    }

    public List<Question> findAll() {
        return List.copyOf(new ArrayList<>(questions.values()));
    }
}
