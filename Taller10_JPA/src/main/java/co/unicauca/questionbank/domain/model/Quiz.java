package co.unicauca.questionbank.domain.model;

import co.unicauca.questionbank.domain.exception.BusinessRuleException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aggregate root. A quiz (simulacro) made of published questions.
 *
 * <p>It is a different aggregate from {@link Question}, so it keeps only the identities of its questions
 * and of its creator, not the objects themselves. The rule "only PUBLISHED questions" is checked when a
 * question is added, using the question the caller hands in.
 */
public class Quiz extends DomainEntity {
    private final String title;
    private final Long creatorId;
    private final List<Long> questionIds = new ArrayList<>();

    /** Creates an empty quiz. @param id identity or null @param title quiz title @param creatorId identity of the teacher who creates it */
    public Quiz(Long id, String title, Long creatorId) {
        super(id);
        this.title = Rules.requireText(title, "title");
        this.creatorId = Rules.requireNonNull(creatorId, "creatorId");
    }

    /**
     * Rebuilds a stored quiz. The PUBLISHED rule is not re-checked: a question may have been
     * archived after it was added, and that must not make the quiz unreadable.
     */
    public Quiz(Long id, String title, Long creatorId, List<Long> storedQuestionIds) {
        this(id, title, creatorId);
        this.questionIds.addAll(storedQuestionIds);
    }

    /** Adds a question; only stored PUBLISHED questions are allowed, and never twice. @param question question to add */
    public void addQuestion(Question question) {
        Rules.requireNonNull(question, "question");
        Long questionId = Rules.requireNonNull(question.getId(), "question id");
        if (question.getStatus() != QuestionStatus.PUBLISHED) {
            throw new BusinessRuleException("Only PUBLISHED questions can be part of a quiz; question " + questionId + " is " + question.getStatus());
        }
        if (questionIds.contains(questionId)) throw new BusinessRuleException("Question " + questionId + " is already in the quiz");
        questionIds.add(questionId);
    }

    /** @return title */
    public String getTitle() { return title; }
    /** @return identity of the creator (a {@link User} aggregate) */
    public Long getCreatorId() { return creatorId; }
    /** @return identities of the questions, in order (read-only view) */
    public List<Long> getQuestionIds() { return Collections.unmodifiableList(questionIds); }
}
