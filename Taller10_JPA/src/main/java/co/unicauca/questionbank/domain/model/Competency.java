package co.unicauca.questionbank.domain.model;

/** A competency evaluated by questions (a question can evaluate several, and vice versa). Aggregate root. */
public class Competency extends DomainEntity {
    private final String name;
    private final String description;

    /** Creates a competency. @param id identity (null if not stored yet) @param name unique name @param description optional description */
    public Competency(Long id, String name, String description) {
        super(id);
        this.name = Rules.requireText(name, "name");
        this.description = description;
    }
    /** @return competency name */
    public String getName() { return name; }
    /** @return description, may be null */
    public String getDescription() { return description; }
}
