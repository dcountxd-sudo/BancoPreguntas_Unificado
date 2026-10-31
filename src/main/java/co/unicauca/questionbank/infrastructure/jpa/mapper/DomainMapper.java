package co.unicauca.questionbank.infrastructure.jpa.mapper;

import co.unicauca.questionbank.domain.model.*;
import co.unicauca.questionbank.infrastructure.jpa.record.*;
import java.util.List;

/**
 * Converts JPA records into technology-independent domain objects. It must be called inside a
 * transaction, because it walks lazy associations; the domain objects it returns are fully loaded.
 * The opposite direction (domain to record) needs to look up existing rows, so each repository
 * implementation does it.
 */
public final class DomainMapper {
    private DomainMapper() {}

    /** Converts a user record. */
    public static User toDomain(UserRecord r) { return new User(r.getId(), r.getUsername(), r.getFullName(), r.getEmail(), r.getRole()); }
    /** Converts a topic record. */
    public static Topic toDomain(TopicRecord r) { return new Topic(r.getId(), r.getName()); }
    /** Converts a competency record. */
    public static Competency toDomain(CompetencyRecord r) { return new Competency(r.getId(), r.getName(), r.getDescription()); }

    /** Converts a question record into the matching domain subclass, restoring its lifecycle state. */
    public static Question toDomain(QuestionRecord r) {
        if (r instanceof MultipleChoiceQuestionRecord mc) {
            MultipleChoiceQuestion.Builder builder = common(MultipleChoiceQuestion.builder(), r);
            mc.getOptions().forEach(o -> builder.option(o.getLabel(), o.getText(), o.isCorrect()));
            return builder.build();
        }
        if (r instanceof TrueFalseQuestionRecord tf) {
            return common(TrueFalseQuestion.builder(), r).expectedValue(tf.getExpectedValue()).build();
        }
        throw new IllegalArgumentException("Unsupported question record: " + r.getClass().getSimpleName());
    }

    private static <B extends Question.Builder<B>> B common(B builder, QuestionRecord r) {
        builder.id(r.getId()).statement(r.getStatement()).difficulty(r.getDifficulty()).status(r.getStatus())
               .author(toDomain(r.getAuthor()));
        if (r.getTopic() != null) builder.topic(toDomain(r.getTopic()));
        r.getCompetencies().forEach(c -> builder.competency(toDomain(c)));
        QuestionMetadataRecord m = r.getMetadata();
        if (m != null) builder.metadata(new QuestionMetadata(m.getCognitiveLevel(), m.getEstimatedSeconds(), m.getEditorialNotes(), m.getLastReviewedAt()));
        return builder;
    }

    /** Converts a quiz record. */
    public static Quiz toDomain(QuizRecord r) {
        List<Long> questionIds = r.getQuestions().stream().map(QuestionRecord::getId).toList();
        return new Quiz(r.getId(), r.getTitle(), r.getCreator().getId(), questionIds);
    }
}
