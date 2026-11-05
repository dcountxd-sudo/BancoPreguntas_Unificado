package co.unicauca.questionbank.infrastructure.jpa.record;

import co.unicauca.questionbank.domain.model.DifficultyLevel;
import co.unicauca.questionbank.domain.model.QuestionStatus;
import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * JPA root of the question hierarchy (JOINED strategy): common columns live in {@code questions},
 * type-specific columns in one table per subclass. It is independent from the domain hierarchy.
 */
@Entity
@Table(name = "questions")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class QuestionRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 2000) private String statement;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private DifficultyLevel difficulty;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private QuestionStatus status;

    /** Many questions - one author. The foreign key author_id is NOT NULL and must point to an existing user. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserRecord author;

    /** Many questions - one topic (optional until the question is classified). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private TopicRecord topic;

    /** Many-to-many with competencies through the join table question_competencies. */
    @ManyToMany
    @JoinTable(name = "question_competencies",
        joinColumns = @JoinColumn(name = "question_id"),
        inverseJoinColumns = @JoinColumn(name = "competency_id"))
    private Set<CompetencyRecord> competencies = new LinkedHashSet<>();

    /** Inverse side of the one-to-one with metadata (optional). */
    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private QuestionMetadataRecord metadata;

    /** Required by JPA. */
    protected QuestionRecord() {}

    /** @return identity */ public Long getId() { return id; }
    /** @return statement */ public String getStatement() { return statement; }
    /** @param statement statement */ public void setStatement(String statement) { this.statement = statement; }
    /** @return difficulty */ public DifficultyLevel getDifficulty() { return difficulty; }
    /** @param difficulty difficulty */ public void setDifficulty(DifficultyLevel difficulty) { this.difficulty = difficulty; }
    /** @return lifecycle status */ public QuestionStatus getStatus() { return status; }
    /** @param status lifecycle status */ public void setStatus(QuestionStatus status) { this.status = status; }
    /** @return author */ public UserRecord getAuthor() { return author; }
    /** @param author author */ public void setAuthor(UserRecord author) { this.author = author; }
    /** @return topic */ public TopicRecord getTopic() { return topic; }
    /** @param topic topic */ public void setTopic(TopicRecord topic) { this.topic = topic; }
    /** @return competencies */ public Set<CompetencyRecord> getCompetencies() { return competencies; }
    /** @return metadata or null */ public QuestionMetadataRecord getMetadata() { return metadata; }

    /** Sets the metadata keeping both sides of the relation in sync. @param metadata metadata or null */
    public void setMetadata(QuestionMetadataRecord metadata) {
        this.metadata = metadata;
        if (metadata != null) metadata.setQuestion(this);
    }
}
