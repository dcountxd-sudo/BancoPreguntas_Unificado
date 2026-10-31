package co.unicauca.questionbank.domain.repository;

import co.unicauca.questionbank.domain.model.Topic;
import java.util.List;
import java.util.Optional;

/** Persistence contract for topics. */
public interface TopicRepository {
    /** Stores a topic. @return the stored topic */
    Topic save(Topic topic);
    /** Finds a topic by identity. */
    Optional<Topic> findById(Long id);
    /** Lists all topics. */
    List<Topic> findAll();
}
