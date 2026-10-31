package co.unicauca.questionbank.infrastructure.jpa.record;

import jakarta.persistence.*;

/** JPA record of a competency. */
@Entity
@Table(name = "competencies")
public class CompetencyRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String name;
    @Column(length = 1000) private String description;

    /** Required by JPA. */
    public CompetencyRecord() {}
    /** Creates a record. */
    public CompetencyRecord(String name, String description) { this.name = name; this.description = description; }
    /** @return identity */ public Long getId() { return id; }
    /** @return name */ public String getName() { return name; }
    /** @return description */ public String getDescription() { return description; }
    /** @param name name */ public void setName(String name) { this.name = name; }
    /** @param description description */ public void setDescription(String description) { this.description = description; }
}
