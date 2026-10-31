package co.unicauca.questionbank.api;

import co.unicauca.questionbank.domain.exception.BusinessRuleException;
import co.unicauca.questionbank.domain.exception.NotFoundException;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Translates domain exceptions into HTTP responses. */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /** A business rule was violated. */
    @ExceptionHandler(BusinessRuleException.class) @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> businessRule(BusinessRuleException e) { return Map.of("error", e.getMessage()); }

    /** A referenced entity does not exist. */
    @ExceptionHandler(NotFoundException.class) @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(NotFoundException e) { return Map.of("error", e.getMessage()); }

    /** A database constraint (unique, not null, foreign key) was violated. */
    @ExceptionHandler(DataIntegrityViolationException.class) @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> constraint(DataIntegrityViolationException e) { return Map.of("error", "A database constraint was violated (duplicated or missing data)"); }
}
