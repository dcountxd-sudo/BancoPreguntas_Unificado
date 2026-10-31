package co.unicauca.questionbank.infrastructure.jpa.record;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA record of the question metadata. It OWNS the one-to-one relation: the foreign key
 * {@code question_id} lives in this table and is UNIQUE, so a question can never have two
 * metadata rows, and a metadata row cannot exist without its question.
 */
@Entity
@Table(name = "question_metadata")
public class QuestionMetadataRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false, unique = true)
    private QuestionRecord question;
    @Column(nullable = false) private String cognitiveLevel;
    @Column(nullable = false) private int estimatedSeconds;
    @Column(length = 2000) private String editorialNotes;
    private LocalDateTime lastReviewedAt;

    /** Required by JPA. */
    public QuestionMetadataRecord() {}
    /** @return identity */ public Long getId() { return id; }
    /** @return owning question */ public QuestionRecord getQuestion() { return question; }
    /** Used by {@link QuestionRecord#setMetadata}. @param question owning question */ void setQuestion(QuestionRecord question) { this.question = question; }
    /** @return cognitive level */ public String getCognitiveLevel() { return cognitiveLevel; }
    /** @return estimated seconds */ public int getEstimatedSeconds() { return estimatedSeconds; }
    /** @return editorial notes */ public String getEditorialNotes() { return editorialNotes; }
    /** @return last review date */ public LocalDateTime getLastReviewedAt() { return lastReviewedAt; }
    /** Copies the values. */
    public void update(String cognitiveLevel, int estimatedSeconds, String editorialNotes, LocalDateTime lastReviewedAt) {
        this.cognitiveLevel = cognitiveLevel; this.estimatedSeconds = estimatedSeconds;
        this.editorialNotes = editorialNotes; this.lastReviewedAt = lastReviewedAt;
    }
}
