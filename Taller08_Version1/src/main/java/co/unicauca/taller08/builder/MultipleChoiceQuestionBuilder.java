package co.unicauca.taller08.builder;

import java.util.ArrayList;
import java.util.List;

import co.unicauca.taller08.domain.Difficultylevel;
import co.unicauca.taller08.domain.Option;
import co.unicauca.taller08.domain.QuestionType;

public class MultipleChoiceQuestionBuilder extends QuestionBuilder {
    private String statement = "¿Cuál de estos lenguajes es orientado a objetos?";
    private Difficultylevel difficultyLevel = Difficultylevel.MEDIUM;
    private final List<Option> options = new ArrayList<>();

    public MultipleChoiceQuestionBuilder() {
        this(
                "¿Cual es el símbolo del carbón en la tabla periodica?",
                Difficultylevel.MEDIUM,
                List.of(
                        new Option("O", false),
                        new Option("C", true),
                        new Option("Li", false),
                        new Option("Fe", false)
                )
        );
    }

    public MultipleChoiceQuestionBuilder(String statement, Difficultylevel difficultyLevel, List<Option> options) {
        this.statement = statement == null ? "¿Cual es el símbolo del carbón en la tabla periodica?" : statement;
        this.difficultyLevel = difficultyLevel == null ? Difficultylevel.MEDIUM : difficultyLevel;
        this.options.clear();
        if (options != null) {
            this.options.addAll(options);
        }
    }

    @Override
    public void buildType() {
        question.setType(QuestionType.MULTIPLE_CHOICE);
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

    public void addOption(Option option) {
        if (option != null) {
            options.add(option);
        }
    }
}
