package co.edu.unicauca.lisw2_t07_g02.builder;

import java.util.List;

import co.edu.unicauca.lisw2_t07_g02.domain.Difficultylevel;
import co.edu.unicauca.lisw2_t07_g02.domain.Option;
import co.edu.unicauca.lisw2_t07_g02.domain.QuestionType;

public class TrueFalseQuestionBuilder extends QuestionBuilder {
    private String statement = "La Tierra gira alrededor del Sol.";
    private Difficultylevel difficultyLevel = Difficultylevel.EASY;
    private final List<Option> options = List.of(
            new Option("Verdadero", true),
            new Option("Falso", false)
    );

    public TrueFalseQuestionBuilder() {
        this("La Tierra gira alrededor del Sol.", Difficultylevel.EASY);
    }

    public TrueFalseQuestionBuilder(String statement, Difficultylevel difficultyLevel) {
        this.statement = statement == null ? "La Tierra gira alrededor del Sol." : statement;
        this.difficultyLevel = difficultyLevel == null ? Difficultylevel.EASY : difficultyLevel;
    }

    @Override
    public void buildType() {
        question.setType(QuestionType.TRUE_FALSE);
    }

    @Override
    public void buildDifficultyLevel() {
        question.setDifficultyLevel(difficultyLevel);
    }

    @Override
    public void buildStatement() {
        question.setStatement(statement);
    }

    @Override
    public void buildOptions() {
        question.setOptions(options);
    }
}
