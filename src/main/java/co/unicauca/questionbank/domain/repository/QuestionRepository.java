package co.unicauca.questionbank.domain.repository;

import co.unicauca.questionbank.domain.model.Question;
import co.unicauca.questionbank.domain.model.QuestionStatus;
import java.util.List;
import java.util.Optional;

/** Persistence contract for questions, expressed without Spring or JPA types. */
public interface QuestionRepository {
    /** Stores a new question or updates an existing one. @return the stored question */
    Question save(Question question);
    /** Finds a question by identity. */
    Optional<Question> findById(Long id);
    /** Lists all questions. */
    List<Question> findAll();
    /** Lists the questions written by an author. */
    List<Question> findByAuthor(Long authorId);
    /** Lists the questions in a lifecycle status. */
    List<Question> findByStatus(QuestionStatus status);
    /** Lists the PUBLISHED questions that evaluate a competency. */
    List<Question> findPublishedByCompetency(Long competencyId);
}
