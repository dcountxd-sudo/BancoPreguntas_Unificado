package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

public class ReviewState extends AbstractQuestionState {
    @Override
    public void approve(Question question) {
        question.setState(new ApprovedState());
    }

    @Override
    public void reject(Question question) {
        question.setState(new DraftState());
    }

    @Override
    public String getName() {
        return "En revisión";
    }
}
