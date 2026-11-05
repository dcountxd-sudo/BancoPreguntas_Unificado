package co.unicauca.questionbank.domain.model;

import co.unicauca.questionbank.domain.exception.BusinessRuleException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Single-answer multiple-choice question: four distractors and one correct answer,
 * as required by the academic rules of the system.
 */
public class MultipleChoiceQuestion extends Question {
    /** Number of distractors (wrong options) every question must have. */
    public static final int DISTRACTORS = 4;
    /** Total number of options: the distractors plus the correct one. */
    public static final int OPTION_COUNT = DISTRACTORS + 1;

    private final List<AnswerOption> options = new ArrayList<>();

    private MultipleChoiceQuestion(Builder b) {
        super(b);
        this.options.addAll(b.options);
    }

    /** @return a new builder */
    public static Builder builder() { return new Builder(); }

    /** @return the options (read-only view) */
    public List<AnswerOption> getOptions() { return Collections.unmodifiableList(options); }

    /** Replaces the options of a draft question. @param newOptions new options */
    public void replaceOptions(List<AnswerOption> newOptions) {
        ensureEditable();
        options.clear();
        options.addAll(newOptions);
    }

    @Override
    protected void validateContent() {
        if (options.size() != OPTION_COUNT) {
            throw new BusinessRuleException("A multiple-choice question needs exactly " + OPTION_COUNT + " options (" + DISTRACTORS + " distractors and 1 correct answer)");
        }
        long correct = options.stream().filter(AnswerOption::isCorrect).count();
        if (correct != 1) throw new BusinessRuleException("Exactly one option must be correct, found " + correct);
        Set<String> labels = new HashSet<>();
        for (AnswerOption o : options) {
            if (!labels.add(o.getLabel().toUpperCase())) throw new BusinessRuleException("Duplicated option label: " + o.getLabel());
        }
    }

    /** Builder of {@link MultipleChoiceQuestion}. */
    public static final class Builder extends Question.Builder<Builder> {
        private final List<AnswerOption> options = new ArrayList<>();
        @Override protected Builder self() { return this; }

        /** @param option an answer option @return this builder */
        public Builder option(AnswerOption option) { options.add(option); return this; }
        /** @param label option label @param text option text @param correct true if it is the right answer @return this builder */
        public Builder option(String label, String text, boolean correct) { return option(new AnswerOption(label, text, correct)); }
        /** @param all answer options @return this builder */
        public Builder options(List<AnswerOption> all) { options.addAll(all); return this; }
        /** @return the question; common fields are validated here, content rules when submitting for review */
        public MultipleChoiceQuestion build() { return new MultipleChoiceQuestion(this); }
    }
}
