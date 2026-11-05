package co.unicauca.questionbank.domain.model;

/**
 * Base class of every domain entity: an object defined by its identity, not by its attribute values.
 * The entities that guard an aggregate are its aggregate roots (see the class comment of each one).
 *
 * <p>Two entities are equal when they have the same class and the same non-null identity. While the
 * identity is still {@code null} (not stored yet) only the very same instance is equal to itself.
 * The hash code depends only on the class, so it does not change when the repository assigns the identity.
 */
public abstract class DomainEntity {
    private Long id;

    /** @param id identity, or null if the entity has not been stored yet */
    protected DomainEntity(Long id) { this.id = id; }

    /** @return identity assigned by the repository, null before the first save */
    public Long getId() { return id; }

    /** Sets the identity; used by the persistence side after the first save. @param id identifier */
    public void setId(Long id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainEntity other = (DomainEntity) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
