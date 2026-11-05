package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

public class DraftState extends AbstractQuestionState {
    @Override
    public void submitForReview(Question question) {
        question.validate();
        question.setState(new PendingReviewState());
    }

    @Override
    public String getName() {
        return "Borrador";
    }
}
