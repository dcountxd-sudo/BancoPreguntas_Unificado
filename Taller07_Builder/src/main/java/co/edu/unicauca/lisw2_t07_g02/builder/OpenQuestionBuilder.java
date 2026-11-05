package co.edu.unicauca.lisw2_t07_g02.builder;

import co.edu.unicauca.lisw2_t07_g02.domain.Difficultylevel;
import co.edu.unicauca.lisw2_t07_g02.domain.QuestionType;

public class OpenQuestionBuilder extends QuestionBuilder {
    private String statement = "¿Cuál es la capital de Colombia?";
    private Difficultylevel difficultyLevel = Difficultylevel.MEDIUM;

    public OpenQuestionBuilder() {
        this("¿Cuál es la capital de Colombia?", Difficultylevel.MEDIUM);
    }

    public OpenQuestionBuilder(String statement, Difficultylevel difficultyLevel) {
        this.statement = statement == null ? "¿Cuál es la capital de Colombia?" : statement;
        this.difficultyLevel = difficultyLevel == null ? Difficultylevel.MEDIUM : difficultyLevel;
    }

    @Override
    public void buildType() {
        question.setType(QuestionType.OPEN);
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
        question.getOptions().clear();
    }
}
