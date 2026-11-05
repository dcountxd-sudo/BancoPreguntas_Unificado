package co.unicauca.questionbank.domain.repository;

import co.unicauca.questionbank.domain.model.Quiz;
import java.util.List;
import java.util.Optional;

/** Persistence contract for quizzes. */
public interface QuizRepository {
    /** Stores a quiz. @return the stored quiz */
    Quiz save(Quiz quiz);
    /** Finds a quiz by identity. */
    Optional<Quiz> findById(Long id);
    /** Lists all quizzes. */
    List<Quiz> findAll();
}
