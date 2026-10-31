package co.unicauca.questionbank.infrastructure.jpa.repository;

import co.unicauca.questionbank.infrastructure.jpa.record.QuizRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data interface for quizzes. */
public interface SpringQuizDataRepository extends JpaRepository<QuizRecord, Long> {}
