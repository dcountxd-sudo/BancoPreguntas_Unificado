package co.unicauca.questionbank;

import co.unicauca.questionbank.domain.model.*;
import java.util.List;

/** Factories of valid domain objects shared by the tests. */
public final class TestData {
    private TestData() {}

    public static User author() { return new User(1L, "ana", "Ana Author", "ana@x.co", Role.AUTHOR); }
    public static User teacher() { return new User(2L, "tom", "Tom Teacher", "tom@x.co", Role.TEACHER); }
    public static Topic topic() { return new Topic(1L, "Software architecture"); }
    public static Competency competency(long id) { return new Competency(id, "Competency " + id, null); }

    /** Five options, exactly one correct (B). */
    public static List<AnswerOption> validOptions() {
        return List.of(new AnswerOption("A", "Singleton", false), new AnswerOption("B", "Strategy", true),
            new AnswerOption("C", "Facade", false), new AnswerOption("D", "Proxy", false), new AnswerOption("E", "Flyweight", false));
    }

    /** A complete multiple-choice builder that passes every review rule. */
    public static MultipleChoiceQuestion.Builder validMultipleChoice() {
        return MultipleChoiceQuestion.builder().statement("Which pattern selects an algorithm at runtime?")
            .author(author()).topic(topic()).competency(competency(1)).difficulty(DifficultyLevel.MEDIUM).options(validOptions());
    }

    /** A complete true/false builder that passes every review rule. */
    public static TrueFalseQuestion.Builder validTrueFalse() {
        return TrueFalseQuestion.builder().statement("A facade hides the complexity of a subsystem.")
            .author(author()).topic(topic()).competency(competency(1)).expectedValue(true);
    }
}
