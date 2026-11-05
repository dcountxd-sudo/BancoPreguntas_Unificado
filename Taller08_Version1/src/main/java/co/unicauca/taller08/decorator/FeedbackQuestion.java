package co.unicauca.taller08.decorator;

public class FeedbackQuestion extends QuestionDecorator {
    private final String feedback;

    public FeedbackQuestion(QuestionComponent question, String feedback) {
        super(question);
        if (feedback == null || feedback.isBlank()) {
            throw new IllegalArgumentException("La retroalimentación no puede estar vacía.");
        }
        this.feedback = feedback;
    }

    @Override
    public String getDescription() {
        return question.getDescription() + " [RETROALIMENTACIÓN: " + feedback + "]";
    }
}
