package co.unicauca.questionbank.domain.model;

/** Rebuilds the state object that matches a persisted status (the database only stores the status). */
public final class QuestionStates {
    private QuestionStates() {}

    /** Returns the state object for a status. @param status the status @return matching state */
    public static QuestionState of(QuestionStatus status) {
        return switch (Rules.requireNonNull(status, "status")) {
            case DRAFT -> new DraftState();
            case PENDING_REVIEW -> new PendingReviewState();
            case IN_REVIEW -> new InReviewState();
            case APPROVED -> new ApprovedState();
            case PUBLISHED -> new PublishedState();
            case ARCHIVED -> new ArchivedState();
        };
    }
}
