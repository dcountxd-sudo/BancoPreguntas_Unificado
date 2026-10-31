package co.unicauca.questionbank.api;

import co.unicauca.questionbank.api.ApiModels.*;
import co.unicauca.questionbank.application.CatalogService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Reference data endpoints: users, topics and competencies. */
@RestController
@RequestMapping("/api")
public class CatalogController {
    private final CatalogService catalog;

    /** @param catalog catalog use cases */
    public CatalogController(CatalogService catalog) { this.catalog = catalog; }

    /** Creates a user. */
    @PostMapping("/users") @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody CreateUserRequest r) { return UserResponse.from(catalog.createUser(r.username(), r.fullName(), r.email(), r.role())); }
    /** Lists users. */
    @GetMapping("/users")
    public List<UserResponse> users() { return catalog.users().stream().map(UserResponse::from).toList(); }
    /** Creates a topic. */
    @PostMapping("/topics") @ResponseStatus(HttpStatus.CREATED)
    public TopicResponse createTopic(@RequestBody CreateTopicRequest r) { return TopicResponse.from(catalog.createTopic(r.name())); }
    /** Lists topics. */
    @GetMapping("/topics")
    public List<TopicResponse> topics() { return catalog.topics().stream().map(TopicResponse::from).toList(); }
    /** Creates a competency. */
    @PostMapping("/competencies") @ResponseStatus(HttpStatus.CREATED)
    public CompetencyResponse createCompetency(@RequestBody CreateCompetencyRequest r) { return CompetencyResponse.from(catalog.createCompetency(r.name(), r.description())); }
    /** Lists competencies. */
    @GetMapping("/competencies")
    public List<CompetencyResponse> competencies() { return catalog.competencies().stream().map(CompetencyResponse::from).toList(); }
}
