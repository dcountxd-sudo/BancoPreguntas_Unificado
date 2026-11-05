package co.unicauca.questionbank.domain.repository;

import co.unicauca.questionbank.domain.model.User;
import java.util.List;
import java.util.Optional;

/** Persistence contract for users. */
public interface UserRepository {
    /** Stores a user. @return the stored user */
    User save(User user);
    /** Finds a user by identity. */
    Optional<User> findById(Long id);
    /** Lists all users. */
    List<User> findAll();
}
