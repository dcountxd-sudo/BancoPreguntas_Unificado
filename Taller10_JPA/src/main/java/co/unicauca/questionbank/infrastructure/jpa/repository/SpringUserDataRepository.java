package co.unicauca.questionbank.infrastructure.jpa.repository;

import co.unicauca.questionbank.infrastructure.jpa.record.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data interface for users; only the JPA repository implementations use it. */
public interface SpringUserDataRepository extends JpaRepository<UserRecord, Long> {}
