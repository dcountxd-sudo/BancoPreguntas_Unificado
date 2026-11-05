package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

public class PendingReviewState extends AbstractQuestionState {
    @Override
    public void assignReviewer(Question question) {
        question.setState(new ReviewState());
    }

    @Override
    public String getName() {
        return "Pendiente de revisión";
    }
}
