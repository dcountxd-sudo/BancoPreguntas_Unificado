package co.unicauca.questionbank.application;

import co.unicauca.questionbank.domain.model.*;
import co.unicauca.questionbank.domain.repository.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory implementations of the repository contracts. They prove that the application layer
 * can be tested without JPA: it only depends on the interfaces of the domain package.
 */
public final class InMemoryRepositories {
    private InMemoryRepositories() {}

    public static class Users implements UserRepository {
        private final Map<Long, User> data = new LinkedHashMap<>(); private final AtomicLong seq = new AtomicLong();
        @Override public User save(User u) { if (u.getId() == null) u.setId(seq.incrementAndGet()); data.put(u.getId(), u); return u; }
        @Override public Optional<User> findById(Long id) { return Optional.ofNullable(data.get(id)); }
        @Override public List<User> findAll() { return new ArrayList<>(data.values()); }
    }
    public static class Topics implements TopicRepository {
        private final Map<Long, Topic> data = new LinkedHashMap<>(); private final AtomicLong seq = new AtomicLong();
        @Override public Topic save(Topic t) { if (t.getId() == null) t.setId(seq.incrementAndGet()); data.put(t.getId(), t); return t; }
        @Override public Optional<Topic> findById(Long id) { return Optional.ofNullable(data.get(id)); }
        @Override public List<Topic> findAll() { return new ArrayList<>(data.values()); }
    }
    public static class Competencies implements CompetencyRepository {
        private final Map<Long, Competency> data = new LinkedHashMap<>(); private final AtomicLong seq = new AtomicLong();
        @Override public Competency save(Competency c) { if (c.getId() == null) c.setId(seq.incrementAndGet()); data.put(c.getId(), c); return c; }
        @Override public Optional<Competency> findById(Long id) { return Optional.ofNullable(data.get(id)); }
        @Override public List<Competency> findAllById(Collection<Long> ids) { return ids.stream().map(data::get).filter(Objects::nonNull).toList(); }
        @Override public List<Competency> findAll() { return new ArrayList<>(data.values()); }
    }
    public static class Questions implements QuestionRepository {
        private final Map<Long, Question> data = new LinkedHashMap<>(); private final AtomicLong seq = new AtomicLong();
        @Override public Question save(Question q) { if (q.getId() == null) q.setId(seq.incrementAndGet()); data.put(q.getId(), q); return q; }
        @Override public Optional<Question> findById(Long id) { return Optional.ofNullable(data.get(id)); }
        @Override public List<Question> findAll() { return new ArrayList<>(data.values()); }
        @Override public List<Question> findByAuthor(Long id) { return data.values().stream().filter(q -> q.getAuthor().getId().equals(id)).toList(); }
        @Override public List<Question> findByStatus(QuestionStatus s) { return data.values().stream().filter(q -> q.getStatus() == s).toList(); }
        @Override public List<Question> findPublishedByCompetency(Long cid) {
            return data.values().stream().filter(q -> q.getStatus() == QuestionStatus.PUBLISHED
                && q.getCompetencies().stream().anyMatch(c -> c.getId().equals(cid))).toList();
        }
    }
    public static class Quizzes implements QuizRepository {
        private final Map<Long, Quiz> data = new LinkedHashMap<>(); private final AtomicLong seq = new AtomicLong();
        @Override public Quiz save(Quiz q) { if (q.getId() == null) q.setId(seq.incrementAndGet()); data.put(q.getId(), q); return q; }
        @Override public Optional<Quiz> findById(Long id) { return Optional.ofNullable(data.get(id)); }
        @Override public List<Quiz> findAll() { return new ArrayList<>(data.values()); }
    }
}
