package co.unicauca.questionbank.infrastructure.jpa;

import co.unicauca.questionbank.domain.exception.NotFoundException;
import co.unicauca.questionbank.domain.model.Topic;
import co.unicauca.questionbank.domain.repository.TopicRepository;
import co.unicauca.questionbank.infrastructure.jpa.mapper.DomainMapper;
import co.unicauca.questionbank.infrastructure.jpa.record.TopicRecord;
import co.unicauca.questionbank.infrastructure.jpa.repository.SpringTopicDataRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** JPA implementation of the {@link TopicRepository} contract. */
@Repository
public class TopicRepositoryJpa implements TopicRepository {
    private final SpringTopicDataRepository data;

    /** @param data Spring Data repository */
    public TopicRepositoryJpa(SpringTopicDataRepository data) { this.data = data; }

    @Override @Transactional
    public Topic save(Topic topic) {
        TopicRecord record = topic.getId() == null ? new TopicRecord()
            : data.findById(topic.getId()).orElseThrow(() -> new NotFoundException("Topic not found: " + topic.getId()));
        record.setName(topic.getName());
        return DomainMapper.toDomain(data.saveAndFlush(record));
    }
    @Override @Transactional(readOnly = true)
    public Optional<Topic> findById(Long id) { return id == null ? Optional.empty() : data.findById(id).map(DomainMapper::toDomain); }
    @Override @Transactional(readOnly = true)
    public List<Topic> findAll() { return data.findAll().stream().map(DomainMapper::toDomain).toList(); }
}
