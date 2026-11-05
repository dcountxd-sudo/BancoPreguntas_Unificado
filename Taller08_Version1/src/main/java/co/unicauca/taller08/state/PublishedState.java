package co.unicauca.taller08.state;

import co.unicauca.taller08.domain.Question;

public class PublishedState extends AbstractQuestionState {
    @Override
    public void archive(Question question) {
        question.setState(new ArchivedState());
    }

    @Override
    public String getName() {
        return "Publicada";
    }

    @Override
    public boolean isUsableInAssessment() {
        return true;
    }
}
