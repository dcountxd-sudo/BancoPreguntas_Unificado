package co.unicauca.questionbank.domain.exception;

/** Thrown when a referenced entity does not exist. */
public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /** @param message description of the missing entity */
    public NotFoundException(String message) { super(message); }
}
