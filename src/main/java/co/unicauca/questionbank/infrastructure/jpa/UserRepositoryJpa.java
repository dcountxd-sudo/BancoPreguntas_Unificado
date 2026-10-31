package co.unicauca.questionbank.infrastructure.jpa;

import co.unicauca.questionbank.domain.exception.NotFoundException;
import co.unicauca.questionbank.domain.model.User;
import co.unicauca.questionbank.domain.repository.UserRepository;
import co.unicauca.questionbank.infrastructure.jpa.mapper.DomainMapper;
import co.unicauca.questionbank.infrastructure.jpa.record.UserRecord;
import co.unicauca.questionbank.infrastructure.jpa.repository.SpringUserDataRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** JPA implementation of the {@link UserRepository} contract. */
@Repository
public class UserRepositoryJpa implements UserRepository {
    private final SpringUserDataRepository data;

    /** @param data Spring Data repository */
    public UserRepositoryJpa(SpringUserDataRepository data) { this.data = data; }

    @Override @Transactional
    public User save(User user) {
        UserRecord record = user.getId() == null ? new UserRecord()
            : data.findById(user.getId()).orElseThrow(() -> new NotFoundException("User not found: " + user.getId()));
        record.setUsername(user.getUsername()); record.setFullName(user.getFullName());
        record.setEmail(user.getEmail()); record.setRole(user.getRole());
        return DomainMapper.toDomain(data.saveAndFlush(record));
    }
    @Override @Transactional(readOnly = true)
    public Optional<User> findById(Long id) { return id == null ? Optional.empty() : data.findById(id).map(DomainMapper::toDomain); }
    @Override @Transactional(readOnly = true)
    public List<User> findAll() { return data.findAll().stream().map(DomainMapper::toDomain).toList(); }
}
