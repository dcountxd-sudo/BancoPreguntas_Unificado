package co.unicauca.questionbank.infrastructure.jpa.record;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/** JPA record of a quiz; many-to-many with questions through quiz_questions, keeping the order. */
@Entity
@Table(name = "quizzes")
public class QuizRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String title;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private UserRecord creator;
    @ManyToMany
    @JoinTable(name = "quiz_questions",
        joinColumns = @JoinColumn(name = "quiz_id"),
        inverseJoinColumns = @JoinColumn(name = "question_id"))
    @OrderColumn(name = "position")
    private List<QuestionRecord> questions = new ArrayList<>();

    /** Required by JPA. */
    public QuizRecord() {}
    /** @return identity */ public Long getId() { return id; }
    /** @return title */ public String getTitle() { return title; }
    /** @param title title */ public void setTitle(String title) { this.title = title; }
    /** @return creator */ public UserRecord getCreator() { return creator; }
    /** @param creator creator */ public void setCreator(UserRecord creator) { this.creator = creator; }
    /** @return questions in order */ public List<QuestionRecord> getQuestions() { return questions; }
}
