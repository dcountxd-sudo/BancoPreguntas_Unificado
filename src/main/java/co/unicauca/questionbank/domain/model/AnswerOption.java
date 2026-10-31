package co.unicauca.questionbank.domain.model;

import java.util.Objects;

/**
 * Value object: one answer option of a multiple-choice question. It is immutable and has no identity,
 * so two options with the same label, text and correctness are interchangeable.
 */
public final class AnswerOption {
    private final String label;
    private final String text;
    private final boolean correct;

    /** Creates an option. @param label short label such as "A" @param text option text @param correct true for the right answer */
    public AnswerOption(String label, String text, boolean correct) {
        this.label = Rules.requireText(label, "label");
        this.text = Rules.requireText(text, "text");
        this.correct = correct;
    }
    /** @return label */
    public String getLabel() { return label; }
    /** @return option text */
    public String getText() { return text; }
    /** @return true if this is the correct answer */
    public boolean isCorrect() { return correct; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnswerOption other)) return false;
        return correct == other.correct && label.equals(other.label) && text.equals(other.text);
    }
    @Override public int hashCode() { return Objects.hash(label, text, correct); }
}
