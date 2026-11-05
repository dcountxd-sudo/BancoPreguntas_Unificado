package co.unicauca.questionbank.domain.model;

import co.unicauca.questionbank.domain.exception.BusinessRuleException;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value object: complementary data of a question (one-to-one with {@link Question}). Immutable, no identity;
 * it lives inside the {@link Question} aggregate and is compared by value.
 */
public final class QuestionMetadata {
    private final String cognitiveLevel;
    private final int estimatedSeconds;
    private final String editorialNotes;
    private final LocalDateTime lastReviewedAt;

    /**
     * Creates metadata.
     * @param cognitiveLevel cognitive level (for example "Apply")
     * @param estimatedSeconds estimated answer time, must be positive
     * @param editorialNotes free editorial notes, may be null
     * @param lastReviewedAt date of the last review, may be null
     */
    public QuestionMetadata(String cognitiveLevel, int estimatedSeconds, String editorialNotes, LocalDateTime lastReviewedAt) {
        this.cognitiveLevel = Rules.requireText(cognitiveLevel, "cognitiveLevel");
        if (estimatedSeconds <= 0) throw new BusinessRuleException("The estimated time must be positive");
        this.estimatedSeconds = estimatedSeconds;
        this.editorialNotes = editorialNotes;
        this.lastReviewedAt = lastReviewedAt;
    }
    /** @return cognitive level */
    public String getCognitiveLevel() { return cognitiveLevel; }
    /** @return estimated answer time in seconds */
    public int getEstimatedSeconds() { return estimatedSeconds; }
    /** @return editorial notes, may be null */
    public String getEditorialNotes() { return editorialNotes; }
    /** @return last review date, may be null */
    public LocalDateTime getLastReviewedAt() { return lastReviewedAt; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionMetadata m)) return false;
        return estimatedSeconds == m.estimatedSeconds && cognitiveLevel.equals(m.cognitiveLevel)
            && Objects.equals(editorialNotes, m.editorialNotes) && Objects.equals(lastReviewedAt, m.lastReviewedAt);
    }
    @Override public int hashCode() { return Objects.hash(cognitiveLevel, estimatedSeconds, editorialNotes, lastReviewedAt); }
}
