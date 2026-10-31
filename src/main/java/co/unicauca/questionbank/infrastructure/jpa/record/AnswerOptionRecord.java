package co.unicauca.questionbank.infrastructure.jpa.record;

import jakarta.persistence.*;

/** JPA record of an answer option; it owns the many-to-one side with a NOT NULL foreign key. */
@Entity
@Table(name = "answer_options")
public class AnswerOptionRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "option_label", nullable = false) private String label;
    @Column(name = "option_text", nullable = false, length = 1000) private String text;
    @Column(name = "is_correct", nullable = false) private boolean correct;

    /** An option can never exist without a question. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private MultipleChoiceQuestionRecord question;

    /** Required by JPA. */
    public AnswerOptionRecord() {}
    /** Creates a record. */
    public AnswerOptionRecord(String label, String text, boolean correct) { this.label = label; this.text = text; this.correct = correct; }
    /** @return identity */ public Long getId() { return id; }
    /** @return label */ public String getLabel() { return label; }
    /** @return text */ public String getText() { return text; }
    /** @return true if correct */ public boolean isCorrect() { return correct; }
    /** @return owning question */ public MultipleChoiceQuestionRecord getQuestion() { return question; }
    /** @param question owning question */ public void setQuestion(MultipleChoiceQuestionRecord question) { this.question = question; }
}
