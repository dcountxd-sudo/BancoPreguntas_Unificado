package co.unicauca.taller08.builder;

import co.unicauca.taller08.domain.DifficultyLevel;
import co.unicauca.taller08.domain.Option;
import co.unicauca.taller08.domain.Question;
import co.unicauca.taller08.domain.QuestionType;

import java.util.ArrayList;
import java.util.List;

public class OpenQuestionBuilder extends QuestionBuilder {

    public OpenQuestionBuilder() {
        reset();
    }

    public void reset() {
        question = new Question();
        question.setType(QuestionType.OPEN);
    }

    public void buildStatement(String statement) {
        question.setStatement(statement);
    }

    public void buildContext(String context) {
        question.setContext(context);
    }

    public void buildDifficulty(DifficultyLevel difficulty) {
        question.setDifficulty(difficulty);
    }

    public void buildCategory(String category) {
        question.setCategory(category);
    }

    public void buildSubtopic(String subtopic) {
        question.setSubtopic(subtopic);
    }

    public void buildCompetence(String competence) {
        question.setCompetence(competence);
    }

    public void buildAuthor(String author) {
        question.setAuthor(author);
    }

    public void buildJustification(String justification) {
        question.setJustification(justification);
    }

    public void buildSource(String source) {
        question.setSource(source);
    }

    public void buildAnswers(List<Option> options, String correctAnswer) {

        question.setOptions(new ArrayList<>());
        question.setCorrectAnswer(correctAnswer);
    }

    public Question build() {

        validateCommon();
        validateSpecific();

        Question result = question;

        reset();

        return result;
    }

    private void validateSpecific() {

        if (isBlank(question.getCorrectAnswer())) {
            throw new IllegalStateException(
                    "Una pregunta abierta debe tener una respuesta esperada");
        }
    }
}