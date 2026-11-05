package co.unicauca.questionbank.application;

import co.unicauca.questionbank.domain.exception.BusinessRuleException;
import co.unicauca.questionbank.domain.exception.NotFoundException;
import co.unicauca.questionbank.domain.model.*;
import co.unicauca.questionbank.domain.repository.*;
import co.unicauca.questionbank.domain.service.QuizAssembler;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Use case 5: creates quizzes made of published questions. */
@Service
@Transactional
public class QuizService {
    private final QuizRepository quizzes;
    private final QuestionRepository questions;
    private final UserRepository users;
    private final QuizAssembler assembler;

    /** Injects repository contracts and the domain service. */
    public QuizService(QuizRepository quizzes, QuestionRepository questions, UserRepository users, QuizAssembler assembler) {
        this.quizzes = quizzes; this.questions = questions; this.users = users; this.assembler = assembler;
    }

    /** Creates a quiz: loads the aggregates and lets the domain service apply the rules (TEACHER creator, PUBLISHED questions). */
    public Quiz create(Long creatorId, String title, List<Long> questionIds) {
        User creator = users.findById(creatorId).orElseThrow(() -> new NotFoundException("User not found: " + creatorId));
        List<Question> found = questionIds.stream()
            .map(id -> questions.findById(id).orElseThrow(() -> new NotFoundException("Question not found: " + id))).toList();
        Quiz quiz = assembler.assemble(creator, title, found);
        return quizzes.save(quiz);
    }

    /** Finds a quiz. @throws NotFoundException if it does not exist */
    @Transactional(readOnly = true)
    public Quiz findById(Long id) { return quizzes.findById(id).orElseThrow(() -> new NotFoundException("Quiz not found: " + id)); }
    /** Lists all quizzes. */
    @Transactional(readOnly = true)
    public List<Quiz> findAll() { return quizzes.findAll(); }
}
