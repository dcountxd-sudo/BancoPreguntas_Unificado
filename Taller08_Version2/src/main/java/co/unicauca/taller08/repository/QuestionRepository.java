package co.unicauca.taller08.repository;
import co.unicauca.taller08.domain.Question;
import java.util.*;
/** Repositorio en memoria del banco de preguntas. */
public class QuestionRepository  {
    private final List<Question> data = new ArrayList<>();
    public void save(Question q) {
        if(q==null) throw new IllegalArgumentException("Pregunta obligatoria");
        if(!data.contains(q)) data.add(q);
    }
    public List<Question> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(data));
    }
    public Optional<Question> findByIndex(int index) {
        return index>=0 && index<data.size()?Optional.of(data.get(index)):Optional.empty();
    }
    public int count() {
        return data.size();
    }
}
