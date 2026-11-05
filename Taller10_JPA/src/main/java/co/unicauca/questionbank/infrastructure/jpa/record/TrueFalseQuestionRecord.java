package co.unicauca.questionbank.infrastructure.jpa.record;

import jakarta.persistence.*;

/** JPA record of true/false questions. */
@Entity
@Table(name = "true_false_questions")
public class TrueFalseQuestionRecord extends QuestionRecord {
    @Column(name = "expected_value") private Boolean expectedValue;

    /** Required by JPA. */
    public TrueFalseQuestionRecord() {}
    /** @return expected value */ public Boolean getExpectedValue() { return expectedValue; }
    /** @param expectedValue expected value */ public void setExpectedValue(Boolean expectedValue) { this.expectedValue = expectedValue; }
}
