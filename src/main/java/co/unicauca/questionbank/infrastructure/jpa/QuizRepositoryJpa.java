package co.unicauca.questionbank.infrastructure.jpa;

import co.unicauca.questionbank.domain.exception.NotFoundException;
import co.unicauca.questionbank.domain.model.Question;
import co.unicauca.questionbank.domain.model.Quiz;
import co.unicauca.questionbank.domain.repository.QuizRepository;
import co.unicauca.questionbank.infrastructure.jpa.mapper.DomainMapper;
import co.unicauca.questionbank.infrastructure.jpa.record.QuestionRecord;
import co.unicauca.questionbank.infrastructure.jpa.record.QuizRecord;
import co.unicauca.questionbank.infrastructure.jpa.repository.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** JPA implementation of the {@link QuizRepository} contract. */
@Repository
public class QuizRepositoryJpa implements QuizRepository {
    private final SpringQuizDataRepository quizzes;
    private final SpringQuestionDataRepository questions;
    private final SpringUserDataRepository users;

    /** Injects the Spring Data interfaces. */
    public QuizRepositoryJpa(SpringQuizDataRepository quizzes, SpringQuestionDataRepository questions, SpringUserDataRepository users) {
        this.quizzes = quizzes; this.questions = questions; this.users = users;
    }

    @Override @Transactional
    public Quiz save(Quiz quiz) {
        QuizRecord record = quiz.getId() == null ? new QuizRecord()
            : quizzes.findById(quiz.getId()).orElseThrow(() -> new NotFoundException("Quiz not found: " + quiz.getId()));
        record.setTitle(quiz.getTitle());
        Long creatorId = quiz.getCreatorId();
        record.setCreator(users.findById(creatorId == null ? -1L : creatorId).orElseThrow(() -> new NotFoundException("User not found: " + creatorId)));

        List<Long> ids = quiz.getQuestionIds();
        Map<Long, QuestionRecord> found = questions.findAllById(ids).stream().collect(Collectors.toMap(QuestionRecord::getId, Function.identity()));
        record.getQuestions().clear();
        for (Long id : ids) {
            QuestionRecord q = found.get(id);
            if (q == null) throw new NotFoundException("Question not found: " + id);
            record.getQuestions().add(q);
        }
        return DomainMapper.toDomain(quizzes.saveAndFlush(record));
    }

    @Override @Transactional(readOnly = true)
    public Optional<Quiz> findById(Long id) { return id == null ? Optional.empty() : quizzes.findById(id).map(DomainMapper::toDomain); }
    @Override @Transactional(readOnly = true)
    public List<Quiz> findAll() { return quizzes.findAll().stream().map(DomainMapper::toDomain).toList(); }
}
