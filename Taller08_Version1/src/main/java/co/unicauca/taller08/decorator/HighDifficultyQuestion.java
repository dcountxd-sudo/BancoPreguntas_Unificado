package co.unicauca.taller08.decorator;

public class HighDifficultyQuestion extends QuestionDecorator {
    public HighDifficultyQuestion(QuestionComponent question) {
        super(question);
    }

    @Override
    public String getDescription() {
        return question.getDescription() + " [ALTA DIFICULTAD]";
    }

    @Override
    public double getScore() {
        return question.getScore() + 0.5;
    }
}
