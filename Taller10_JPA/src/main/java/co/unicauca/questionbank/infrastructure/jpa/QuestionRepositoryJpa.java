package co.unicauca.questionbank.infrastructure.jpa;

import co.unicauca.questionbank.domain.exception.NotFoundException;
import co.unicauca.questionbank.domain.model.*;
import co.unicauca.questionbank.domain.repository.QuestionRepository;
import co.unicauca.questionbank.infrastructure.jpa.mapper.DomainMapper;
import co.unicauca.questionbank.infrastructure.jpa.record.*;
import co.unicauca.questionbank.infrastructure.jpa.repository.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA implementation of the {@link QuestionRepository} contract. It translates the domain
 * hierarchy into the independent record hierarchy and resolves every reference (author, topic,
 * competencies) against the database, so a question can never point to something that does not exist.
 */
@Repository
public class QuestionRepositoryJpa implements QuestionRepository {
    private final SpringQuestionDataRepository questions;
    private final SpringUserDataRepository users;
    private final SpringTopicDataRepository topics;
    private final SpringCompetencyDataRepository competencies;

    /** Injects the Spring Data interfaces. */
    public QuestionRepositoryJpa(SpringQuestionDataRepository questions, SpringUserDataRepository users,
                                 SpringTopicDataRepository topics, SpringCompetencyDataRepository competencies) {
        this.questions = questions; this.users = users; this.topics = topics; this.competencies = competencies;
    }

    @Override @Transactional
    public Question save(Question question) {
        QuestionRecord record = question.getId() == null ? newRecord(question)
            : questions.findById(question.getId()).orElseThrow(() -> new NotFoundException("Question not found: " + question.getId()));
        copyInto(record, question);
        return DomainMapper.toDomain(questions.saveAndFlush(record));
    }

    @Override @Transactional(readOnly = true)
    public Optional<Question> findById(Long id) { return id == null ? Optional.empty() : questions.findById(id).map(DomainMapper::toDomain); }
    @Override @Transactional(readOnly = true)
    public List<Question> findAll() { return map(questions.findAll()); }
    @Override @Transactional(readOnly = true)
    public List<Question> findByAuthor(Long authorId) { return map(questions.findByAuthorId(authorId)); }
    @Override @Transactional(readOnly = true)
    public List<Question> findByStatus(QuestionStatus status) { return map(questions.findByStatus(status)); }
    @Override @Transactional(readOnly = true)
    public List<Question> findPublishedByCompetency(Long competencyId) { return map(questions.findByCompetencyAndStatus(competencyId, QuestionStatus.PUBLISHED)); }

    private List<Question> map(List<QuestionRecord> records) { return records.stream().map(DomainMapper::toDomain).toList(); }

    private QuestionRecord newRecord(Question q) {
        if (q instanceof MultipleChoiceQuestion) return new MultipleChoiceQuestionRecord();
        if (q instanceof TrueFalseQuestion) return new TrueFalseQuestionRecord();
        throw new IllegalArgumentException("Unsupported question type: " + q.getClass().getSimpleName());
    }

    private void copyInto(QuestionRecord r, Question q) {
        r.setStatement(q.getStatement());
        r.setDifficulty(q.getDifficulty());
        r.setStatus(q.getStatus());
        Long authorId = q.getAuthor().getId();
        r.setAuthor(users.findById(authorId == null ? -1L : authorId).orElseThrow(() -> new NotFoundException("Author not found: " + authorId)));
        r.setTopic(q.getTopic() == null ? null
            : topics.findById(q.getTopic().getId()).orElseThrow(() -> new NotFoundException("Topic not found: " + q.getTopic().getId())));

        Set<Long> competencyIds = q.getCompetencies().stream().map(Competency::getId).collect(Collectors.toSet());
        List<CompetencyRecord> found = competencies.findAllById(competencyIds);
        if (found.size() != competencyIds.size()) throw new NotFoundException("One or more competencies do not exist: " + competencyIds);
        r.getCompetencies().clear();
        r.getCompetencies().addAll(found);

        QuestionMetadata m = q.getMetadata();
        if (m == null) {
            r.setMetadata(null);
        } else {
            QuestionMetadataRecord mr = r.getMetadata() != null ? r.getMetadata() : new QuestionMetadataRecord();
            mr.update(m.getCognitiveLevel(), m.getEstimatedSeconds(), m.getEditorialNotes(), m.getLastReviewedAt());
            r.setMetadata(mr);
        }

        if (r instanceof MultipleChoiceQuestionRecord mc && q instanceof MultipleChoiceQuestion domain) {
            mc.replaceOptions(domain.getOptions().stream().map(o -> new AnswerOptionRecord(o.getLabel(), o.getText(), o.isCorrect())).toList());
        } else if (r instanceof TrueFalseQuestionRecord tf && q instanceof TrueFalseQuestion domain) {
            tf.setExpectedValue(domain.getExpectedValue());
        }
    }
}
