package co.unicauca.questionbank.api;

import co.unicauca.questionbank.domain.model.*;
import java.util.List;
import java.util.Set;

/** Request and response objects of the REST API. They keep HTTP details out of the domain. */
public final class ApiModels {
    private ApiModels() {}

    //requests
    /** Body to create a user. */
    public record CreateUserRequest(String username, String fullName, String email, Role role) {}
    /** Body to create a topic. */
    public record CreateTopicRequest(String name) {}
    /** Body to create a competency. */
    public record CreateCompetencyRequest(String name, String description) {}
    /** Optional complementary data of a question. */
    public record MetadataRequest(String cognitiveLevel, int estimatedSeconds, String editorialNotes) {
        QuestionMetadata toDomain() { return new QuestionMetadata(cognitiveLevel, estimatedSeconds, editorialNotes, null); }
    }
    /** One answer option in a request. */
    public record OptionRequest(String label, String text, boolean correct) {
        AnswerOption toDomain() { return new AnswerOption(label, text, correct); }
    }
    /** Body to create a multiple-choice question. */
    public record CreateMultipleChoiceRequest(Long authorId, String statement, DifficultyLevel difficulty, Long topicId,
                                              Set<Long> competencyIds, List<OptionRequest> options, MetadataRequest metadata) {}
    /** Body to create a true/false question. */
    public record CreateTrueFalseRequest(Long authorId, String statement, DifficultyLevel difficulty, Long topicId,
                                         Set<Long> competencyIds, Boolean expectedValue, MetadataRequest metadata) {}
    /** Body to classify a question. */
    public record ClassifyRequest(Long topicId, Set<Long> competencyIds) {}
    /** Body to create a quiz. */
    public record CreateQuizRequest(Long creatorId, String title, List<Long> questionIds) {}

    // responses
    /** A user. */
    public record UserResponse(Long id, String username, String fullName, String email, Role role) {
        static UserResponse from(User u) { return new UserResponse(u.getId(), u.getUsername(), u.getFullName(), u.getEmail(), u.getRole()); }
    }
    /** A topic. */
    public record TopicResponse(Long id, String name) {
        static TopicResponse from(Topic t) { return new TopicResponse(t.getId(), t.getName()); }
    }
    /** A competency. */
    public record CompetencyResponse(Long id, String name, String description) {
        static CompetencyResponse from(Competency c) { return new CompetencyResponse(c.getId(), c.getName(), c.getDescription()); }
    }
    /** An answer option. */
    public record OptionResponse(String label, String text, boolean correct) {}
    /** Complementary data. */
    public record MetadataResponse(String cognitiveLevel, int estimatedSeconds, String editorialNotes) {}
    /** A question of any type; fields of the other type are null. */
    public record QuestionResponse(Long id, String type, String statement, DifficultyLevel difficulty, QuestionStatus status,
                                   Long authorId, String topic, List<String> competencies,
                                   List<OptionResponse> options, Boolean expectedValue, MetadataResponse metadata) {
        static QuestionResponse from(Question q) {
            List<OptionResponse> options = null;
            Boolean expected = null;
            String type = q.getClass().getSimpleName();
            if (q instanceof MultipleChoiceQuestion mc) {
                options = mc.getOptions().stream().map(o -> new OptionResponse(o.getLabel(), o.getText(), o.isCorrect())).toList();
            } else if (q instanceof TrueFalseQuestion tf) {
                expected = tf.getExpectedValue();
            }
            QuestionMetadata m = q.getMetadata();
            return new QuestionResponse(q.getId(), type, q.getStatement(), q.getDifficulty(), q.getStatus(), q.getAuthor().getId(),
                q.getTopic() == null ? null : q.getTopic().getName(),
                q.getCompetencies().stream().map(Competency::getName).toList(), options, expected,
                m == null ? null : new MetadataResponse(m.getCognitiveLevel(), m.getEstimatedSeconds(), m.getEditorialNotes()));
        }
    }
    /** A quiz. */
    public record QuizResponse(Long id, String title, Long creatorId, List<Long> questionIds) {
        static QuizResponse from(Quiz quiz) {
            return new QuizResponse(quiz.getId(), quiz.getTitle(), quiz.getCreatorId(), quiz.getQuestionIds());
        }
    }
}
