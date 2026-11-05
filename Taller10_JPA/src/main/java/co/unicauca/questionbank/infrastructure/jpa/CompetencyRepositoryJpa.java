package co.unicauca.questionbank.infrastructure.jpa;

import co.unicauca.questionbank.domain.exception.NotFoundException;
import co.unicauca.questionbank.domain.model.Competency;
import co.unicauca.questionbank.domain.repository.CompetencyRepository;
import co.unicauca.questionbank.infrastructure.jpa.mapper.DomainMapper;
import co.unicauca.questionbank.infrastructure.jpa.record.CompetencyRecord;
import co.unicauca.questionbank.infrastructure.jpa.repository.SpringCompetencyDataRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** JPA implementation of the {@link CompetencyRepository} contract. */
@Repository
public class CompetencyRepositoryJpa implements CompetencyRepository {
    private final SpringCompetencyDataRepository data;

    /** @param data Spring Data repository */
    public CompetencyRepositoryJpa(SpringCompetencyDataRepository data) { this.data = data; }

    @Override @Transactional
    public Competency save(Competency competency) {
        CompetencyRecord record = competency.getId() == null ? new CompetencyRecord()
            : data.findById(competency.getId()).orElseThrow(() -> new NotFoundException("Competency not found: " + competency.getId()));
        record.setName(competency.getName()); record.setDescription(competency.getDescription());
        return DomainMapper.toDomain(data.saveAndFlush(record));
    }
    @Override @Transactional(readOnly = true)
    public Optional<Competency> findById(Long id) { return id == null ? Optional.empty() : data.findById(id).map(DomainMapper::toDomain); }
    @Override @Transactional(readOnly = true)
    public List<Competency> findAllById(Collection<Long> ids) { return data.findAllById(ids).stream().map(DomainMapper::toDomain).toList(); }
    @Override @Transactional(readOnly = true)
    public List<Competency> findAll() { return data.findAll().stream().map(DomainMapper::toDomain).toList(); }
}
