package co.unicauca.questionbank.domain.exception;

/** Thrown when an operation violates a business rule (invalid transition, invalid content, ...). */
public class BusinessRuleException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /** @param message explanation of the violated rule */
    public BusinessRuleException(String message) { super(message); }
}
