package co.unicauca.questionbank.infrastructure.jpa.repository;

import co.unicauca.questionbank.domain.model.QuestionStatus;
import co.unicauca.questionbank.infrastructure.jpa.record.QuestionRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Spring Data interface for the question hierarchy; queries are polymorphic over the JOINED tables. */
public interface SpringQuestionDataRepository extends JpaRepository<QuestionRecord, Long> {
    /** Questions of one author. */
    List<QuestionRecord> findByAuthorId(Long authorId);
    /** Questions in one status. */
    List<QuestionRecord> findByStatus(QuestionStatus status);
    /** Questions in one status that evaluate a competency. */
    @Query("select distinct q from QuestionRecord q join q.competencies c where c.id = :competencyId and q.status = :status")
    List<QuestionRecord> findByCompetencyAndStatus(@Param("competencyId") Long competencyId, @Param("status") QuestionStatus status);
}
