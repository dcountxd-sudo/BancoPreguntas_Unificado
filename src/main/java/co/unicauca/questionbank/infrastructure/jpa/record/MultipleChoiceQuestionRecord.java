package co.unicauca.questionbank.infrastructure.jpa.record;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/** JPA record of multiple-choice questions; owns the one-to-many relation with its options. */
@Entity
@Table(name = "multiple_choice_questions")
public class MultipleChoiceQuestionRecord extends QuestionRecord {
    /** One question - many options. Options are deleted with the question (cascade + orphanRemoval). */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerOptionRecord> options = new ArrayList<>();

    /** Required by JPA. */
    public MultipleChoiceQuestionRecord() {}
    /** @return options */ public List<AnswerOptionRecord> getOptions() { return options; }

    /** Replaces the options keeping both sides of the relation in sync. @param newOptions new options */
    public void replaceOptions(List<AnswerOptionRecord> newOptions) {
        options.clear();
        newOptions.forEach(o -> { o.setQuestion(this); options.add(o); });
    }
}
