package co.unicauca.questionbank.domain.model;

/** A subject area that groups questions (many questions belong to one topic). Aggregate root. */
public class Topic extends DomainEntity {
    private final String name;

    /** Creates a topic. @param id identity (null if not stored yet) @param name unique topic name */
    public Topic(Long id, String name) { super(id); this.name = Rules.requireText(name, "name"); }
    /** @return topic name */
    public String getName() { return name; }
}
