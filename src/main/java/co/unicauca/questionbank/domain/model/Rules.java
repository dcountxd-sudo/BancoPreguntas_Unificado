package co.unicauca.questionbank.domain.model;

import co.unicauca.questionbank.domain.exception.BusinessRuleException;

/** Small argument checks shared by the domain classes. */
final class Rules {
    private Rules() {}

    /** @return the trimmed text; fails if it is null or blank */
    static String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) throw new BusinessRuleException("The field '" + field + "' is required");
        return value.trim();
    }

    /** @return the value; fails if it is null */
    static <T> T requireNonNull(T value, String field) {
        if (value == null) throw new BusinessRuleException("The field '" + field + "' is required");
        return value;
    }
}
