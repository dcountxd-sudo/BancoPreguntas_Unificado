package co.unicauca.questionbank.domain.model;

import co.unicauca.questionbank.domain.exception.BusinessRuleException;

/**
 * State pattern: lifecycle state of a question (DRAFT, PENDING_REVIEW, IN_REVIEW, APPROVED,
 * PUBLISHED, ARCHIVED). By default every transition is forbidden; each concrete state
 * ({@link DraftState}, {@link PendingReviewState}, {@link InReviewState},
 * {@link ApprovedState}, {@link PublishedState}, {@link ArchivedState}) overrides only the transitions it allows.
 */
public interface QuestionState {
    /** @return the status this state represents */
    QuestionStatus status();

    /** DRAFT to PENDING_REVIEW. @param q the question */
    default void submitForReview(Question q) { deny("submit for review"); }
    /** PENDING_REVIEW to IN_REVIEW (the administrator assigns the reviewers). @param q the question */
    default void assignReviewer(Question q) { deny("assign a reviewer to"); }
    /** IN_REVIEW to APPROVED. @param q the question */
    default void approve(Question q) { deny("approve"); }
    /** IN_REVIEW back to DRAFT so the author can correct the question. @param q the question */
    default void reject(Question q) { deny("reject"); }
    /** APPROVED to PUBLISHED. @param q the question */
    default void publish(Question q) { deny("publish"); }
    /** PUBLISHED to ARCHIVED. @param q the question */
    default void archive(Question q) { deny("archive"); }
    /** @return true if the content of the question can still be edited */
    default boolean isEditable() { return false; }

    private void deny(String operation) {
        throw new BusinessRuleException("Cannot " + operation + " a question in status " + status());
    }
}
