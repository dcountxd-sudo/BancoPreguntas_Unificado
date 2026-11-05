package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

public class ApprovedState extends AbstractQuestionState {
    @Override
    public void publish(Question question) {
        question.setState(new PublishedState());
    }

    @Override
    public String getName() {
        return "Aprobada";
    }
}
