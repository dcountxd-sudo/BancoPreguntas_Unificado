package co.unicauca.questionbank.infrastructure.jpa.repository;

import co.unicauca.questionbank.infrastructure.jpa.record.CompetencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data interface for competencies. */
public interface SpringCompetencyDataRepository extends JpaRepository<CompetencyRecord, Long> {}
