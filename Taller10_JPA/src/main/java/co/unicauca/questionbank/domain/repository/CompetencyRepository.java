package co.unicauca.questionbank.domain.repository;

import co.unicauca.questionbank.domain.model.Competency;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/** Persistence contract for competencies. */
public interface CompetencyRepository {
    /** Stores a competency. @return the stored competency */
    Competency save(Competency competency);
    /** Finds a competency by identity. */
    Optional<Competency> findById(Long id);
    /** Finds the competencies whose identity is in the collection (missing ones are simply not returned). */
    List<Competency> findAllById(Collection<Long> ids);
    /** Lists all competencies. */
    List<Competency> findAll();
}
