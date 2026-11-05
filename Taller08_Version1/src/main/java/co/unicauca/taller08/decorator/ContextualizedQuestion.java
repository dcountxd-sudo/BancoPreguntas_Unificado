package co.unicauca.taller08.decorator;

public class ContextualizedQuestion extends QuestionDecorator {
    public ContextualizedQuestion(QuestionComponent question) {
        super(question);
    }

    @Override
    public String getDescription() {
        return question.getDescription() + " [CONTEXTUALIZADA]";
    }
}
