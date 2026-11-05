package co.unicauca.questionbank.application;

import co.unicauca.questionbank.domain.model.*;
import co.unicauca.questionbank.domain.repository.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Maintains the reference data: users, topics and competencies. */
@Service
@Transactional
public class CatalogService {
    private final UserRepository users;
    private final TopicRepository topics;
    private final CompetencyRepository competencies;

    /** Injects repository contracts. */
    public CatalogService(UserRepository users, TopicRepository topics, CompetencyRepository competencies) {
        this.users = users; this.topics = topics; this.competencies = competencies;
    }
    /** Creates a user. */
    public User createUser(String username, String fullName, String email, Role role) { return users.save(new User(null, username, fullName, email, role)); }
    /** Creates a topic. */
    public Topic createTopic(String name) { return topics.save(new Topic(null, name)); }
    /** Creates a competency. */
    public Competency createCompetency(String name, String description) { return competencies.save(new Competency(null, name, description)); }
    /** Lists users. */
    @Transactional(readOnly = true) public List<User> users() { return users.findAll(); }
    /** Lists topics. */
    @Transactional(readOnly = true) public List<Topic> topics() { return topics.findAll(); }
    /** Lists competencies. */
    @Transactional(readOnly = true) public List<Competency> competencies() { return competencies.findAll(); }
}
