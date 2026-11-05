package co.unicauca.questionbank.domain.model;

/** A person who interacts with the system. Aggregate root; no persistence annotations. */
public class User extends DomainEntity {
    private final String username;
    private final String fullName;
    private final String email;
    private final Role role;

    /** Creates a user. @param id identity (null if not stored yet) @param username unique login @param fullName full name @param email email address @param role role in the system */
    public User(Long id, String username, String fullName, String email, Role role) {
        super(id);
        this.username = Rules.requireText(username, "username");
        this.fullName = Rules.requireText(fullName, "fullName");
        this.email = Rules.requireText(email, "email");
        this.role = Rules.requireNonNull(role, "role");
    }
    /** @return unique login name */
    public String getUsername() { return username; }
    /** @return full name */
    public String getFullName() { return fullName; }
    /** @return email address */
    public String getEmail() { return email; }
    /** @return role of the user */
    public Role getRole() { return role; }

    /** Business rule: only authors write questions. @return true if the user may create questions */
    public boolean canAuthorQuestions() { return role == Role.AUTHOR; }
    /** Business rule: only teachers organise quizzes. @return true if the user may create quizzes */
    public boolean canCreateQuizzes() { return role == Role.TEACHER; }
}
