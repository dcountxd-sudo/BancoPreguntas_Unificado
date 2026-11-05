package co.unicauca.questionbank.infrastructure.jpa.record;

import co.unicauca.questionbank.domain.model.Role;
import jakarta.persistence.*;

/** JPA record of a user. The table is called "users" because USER is a reserved word. */
@Entity
@Table(name = "users")
public class UserRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String username;
    @Column(nullable = false) private String fullName;
    @Column(nullable = false) private String email;
    @Enumerated(EnumType.STRING) @Column(name = "user_role", nullable = false) private Role role;

    /** Required by JPA. */
    public UserRecord() {}
    /** Creates a record. */
    public UserRecord(String username, String fullName, String email, Role role) {
        this.username = username; this.fullName = fullName; this.email = email; this.role = role;
    }
    /** @return identity */ public Long getId() { return id; }
    /** @return login */ public String getUsername() { return username; }
    /** @return full name */ public String getFullName() { return fullName; }
    /** @return email */ public String getEmail() { return email; }
    /** @return role */ public Role getRole() { return role; }
    /** @param username login */ public void setUsername(String username) { this.username = username; }
    /** @param fullName full name */ public void setFullName(String fullName) { this.fullName = fullName; }
    /** @param email email */ public void setEmail(String email) { this.email = email; }
    /** @param role role */ public void setRole(Role role) { this.role = role; }
}
