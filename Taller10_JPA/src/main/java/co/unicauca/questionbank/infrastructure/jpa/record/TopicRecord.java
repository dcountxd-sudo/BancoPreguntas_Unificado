package co.unicauca.questionbank.infrastructure.jpa.record;

import jakarta.persistence.*;

/** JPA record of a topic. */
@Entity
@Table(name = "topics")
public class TopicRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String name;

    /** Required by JPA. */
    public TopicRecord() {}
    /** Creates a record. @param name topic name */
    public TopicRecord(String name) { this.name = name; }
    /** @return identity */ public Long getId() { return id; }
    /** @return name */ public String getName() { return name; }
    /** @param name topic name */ public void setName(String name) { this.name = name; }
}
