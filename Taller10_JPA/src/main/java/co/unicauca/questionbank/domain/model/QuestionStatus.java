package co.unicauca.questionbank.domain.model;

/**
 * Lifecycle statuses of a question: DRAFT, PENDING_REVIEW, IN_REVIEW, APPROVED, PUBLISHED, ARCHIVED.
 * A rejected question goes back to DRAFT.
 */
public enum QuestionStatus { DRAFT, PENDING_REVIEW, IN_REVIEW, APPROVED, PUBLISHED, ARCHIVED }
