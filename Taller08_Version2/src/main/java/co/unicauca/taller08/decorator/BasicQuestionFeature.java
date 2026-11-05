package co.unicauca.taller08.decorator;
import co.unicauca.taller08.domain.Question;
public class BasicQuestionFeature implements QuestionFeature  {
    private final Question question;
    public BasicQuestionFeature(Question q) {
        if(q==null)throw new IllegalArgumentException("Pregunta obligatoria");
        question=q;
    }
    @Override
    public Question getQuestion() {
        return question;
    }
    @Override
    public double getScore() {
        return 1.0;
    }
    @Override
    public String describe() {
        return question.getStatement();
    }
}
