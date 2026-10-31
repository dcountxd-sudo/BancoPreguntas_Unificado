package co.unicauca.questionbank.domain.model;

import co.unicauca.questionbank.domain.exception.BusinessRuleException;

/** True/false question: the expected truth value is the whole answer. */
public class TrueFalseQuestion extends Question {
    private Boolean expectedValue;

    private TrueFalseQuestion(Builder b) {
        super(b);
        this.expectedValue = b.expectedValue;
    }

    /** @return a new builder */
    public static Builder builder() { return new Builder(); }

    /** @return the expected truth value, null while undefined */
    public Boolean getExpectedValue() { return expectedValue; }

    /** Changes the expected value of a draft question. @param value new expected value */
    public void setExpectedValue(boolean value) {
        ensureEditable();
        this.expectedValue = value;
    }

    @Override
    protected void validateContent() {
        if (expectedValue == null) throw new BusinessRuleException("A true/false question must define its expected value");
    }

    /** Builder of {@link TrueFalseQuestion}. */
    public static final class Builder extends Question.Builder<Builder> {
        private Boolean expectedValue;
        @Override protected Builder self() { return this; }

        /** @param value expected truth value @return this builder */
        public Builder expectedValue(Boolean value) { this.expectedValue = value; return this; }
        /** @return the question */
        public TrueFalseQuestion build() { return new TrueFalseQuestion(this); }
    }
}
