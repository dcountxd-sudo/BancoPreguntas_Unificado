package co.unicauca.questionbank.domain.model;

import co.unicauca.questionbank.domain.exception.BusinessRuleException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Aggregate root. Base class of every question. Plain Java object: persistence is handled
 * by separate JPA records. The author, topic and competencies are other aggregates it only refers to.
 *
 * <p>Lifecycle rules live here and in {@link QuestionState}, never in the persistence layer.
 * Content rules that depend on the question type are provided by subclasses through
 * {@link #validateContent()} (Template Method), and instances are created with a builder.
 */
public abstract class Question extends DomainEntity {
    private String statement;
    private DifficultyLevel difficulty;
    private final User author;
    private Topic topic;
    private final Set<Competency> competencies = new LinkedHashSet<>();
    private QuestionMetadata metadata;
    private QuestionState state;

    /** Creates a question from a builder. @param b builder holding the values */
    protected Question(Builder<?> b) {
        super(b.id);
        this.statement = Rules.requireText(b.statement, "statement");
        this.difficulty = Rules.requireNonNull(b.difficulty, "difficulty");
        this.author = Rules.requireNonNull(b.author, "author");
        // Creation rule: only AUTHOR users may create questions. Checked only for new questions (id == null)
        // so that rebuilding stored questions never fails if the author's role changed afterwards.
        if (b.id == null && !author.canAuthorQuestions()) {
            throw new BusinessRuleException("Only users with role AUTHOR can create questions");
        }
        this.topic = b.topic;
        this.metadata = b.metadata;
        this.competencies.addAll(b.competencies);
        this.state = QuestionStates.of(b.status);
    }

    // ---------------------------------------------------------------- lifecycle

    /** Moves DRAFT to PENDING_REVIEW after checking the minimum conditions. */
    public final void submitForReview() { state.submitForReview(this); }
    /** Moves PENDING_REVIEW to IN_REVIEW once the administrator assigns the reviewers. */
    public void assignReviewer() { state.assignReviewer(this); }
    /** Moves IN_REVIEW to APPROVED. */
    public void approve() { state.approve(this); }
    /** Moves IN_REVIEW back to DRAFT so the author can correct the question. */
    public void reject() { state.reject(this); }
    /** Moves APPROVED to PUBLISHED. */
    public void publish() { state.publish(this); }
    /** Moves PUBLISHED to ARCHIVED. */
    public void archive() { state.archive(this); }

    /** Only the lifecycle states may change the state, hence package-private. */
    void moveTo(QuestionState next) { this.state = next; }

    /**
     * Template Method skeleton: common checks first, then the type-specific hook. Called by the DRAFT state,
     * so any other state rejects the transition before looking at the content.
     */
    void ensureReadyForReview() {
        Rules.requireText(statement, "statement");
        Rules.requireNonNull(topic, "topic");
        if (competencies.isEmpty()) throw new BusinessRuleException("The question must evaluate at least one competency");
        validateContent();
    }

    /** Template Method hook: type-specific checks required before review. Throws {@link BusinessRuleException}. */
    protected abstract void validateContent();

    /** Fails unless the question is still a draft. */
    protected final void ensureEditable() {
        if (!state.isEditable()) throw new BusinessRuleException("A question in status " + getStatus() + " cannot be edited");
    }

    // ---------------------------------------------------------------- behaviour

    /** Changes the content of a draft question. @param statement new statement @param difficulty new difficulty */
    public void revise(String statement, DifficultyLevel difficulty) {
        ensureEditable();
        this.statement = Rules.requireText(statement, "statement");
        this.difficulty = Rules.requireNonNull(difficulty, "difficulty");
    }

    /** Classifies the question. @param topic the single topic @param competencies one or more competencies */
    public void classify(Topic topic, Collection<Competency> competencies) {
        Rules.requireNonNull(topic, "topic");
        if (competencies == null || competencies.isEmpty()) throw new BusinessRuleException("At least one competency is required");
        ensureEditable();
        this.topic = topic;
        this.competencies.clear();
        this.competencies.addAll(competencies);
    }

    /** Replaces the complementary data. @param metadata new metadata, may be null */
    public void setMetadata(QuestionMetadata metadata) { this.metadata = metadata; }

    // ---------------------------------------------------------------- accessors

    /** @return statement */
    public String getStatement() { return statement; }
    /** @return difficulty */
    public DifficultyLevel getDifficulty() { return difficulty; }
    /** @return author of the question */
    public User getAuthor() { return author; }
    /** @return topic, null while the question is unclassified */
    public Topic getTopic() { return topic; }
    /** @return competencies evaluated by the question (read-only view) */
    public Set<Competency> getCompetencies() { return java.util.Collections.unmodifiableSet(competencies); }
    /** @return complementary data, may be null */
    public QuestionMetadata getMetadata() { return metadata; }
    /** @return current lifecycle status */
    public QuestionStatus getStatus() { return state.status(); }

    // ---------------------------------------------------------------- builder

    /**
     * Builder shared by all question types. Subclasses add their own fields and a {@code build()} method.
     * @param <B> concrete builder type (self type, so the fluent calls keep returning it)
     */
    public abstract static class Builder<B extends Builder<B>> {
        Long id;
        String statement;
        DifficultyLevel difficulty = DifficultyLevel.MEDIUM;
        QuestionStatus status = QuestionStatus.DRAFT;
        User author;
        Topic topic;
        QuestionMetadata metadata;
        final Set<Competency> competencies = new LinkedHashSet<>();

        /** @return this builder with its concrete type */
        protected abstract B self();

        /** @param id identity (used when rebuilding a stored question) @return this builder */
        public B id(Long id) { this.id = id; return self(); }
        /** @param statement question statement @return this builder */
        public B statement(String statement) { this.statement = statement; return self(); }
        /** @param difficulty difficulty level @return this builder */
        public B difficulty(DifficultyLevel difficulty) { this.difficulty = difficulty; return self(); }
        /** @param status lifecycle status (used when rebuilding a stored question; new ones are DRAFT) @return this builder */
        public B status(QuestionStatus status) { this.status = status; return self(); }
        /** @param author author of the question @return this builder */
        public B author(User author) { this.author = author; return self(); }
        /** @param topic topic of the question @return this builder */
        public B topic(Topic topic) { this.topic = topic; return self(); }
        /** @param metadata complementary data @return this builder */
        public B metadata(QuestionMetadata metadata) { this.metadata = metadata; return self(); }
        /** @param competency a competency evaluated by the question @return this builder */
        public B competency(Competency competency) { this.competencies.add(competency); return self(); }
        /** @param competencies competencies evaluated by the question @return this builder */
        public B competencies(Collection<Competency> competencies) { this.competencies.addAll(competencies); return self(); }
    }
}
