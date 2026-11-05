package co.unicauca.taller08.decorator;

public abstract class QuestionDecorator implements QuestionComponent {
    protected final QuestionComponent question;

    protected QuestionDecorator(QuestionComponent question) {
        if (question == null) {
            throw new IllegalArgumentException("El componente de pregunta es obligatorio.");
        }
        this.question = question;
    }

    @Override
    public String getDescription() {
        return question.getDescription();
    }

    @Override
    public double getScore() {
        return question.getScore();
    }
}
