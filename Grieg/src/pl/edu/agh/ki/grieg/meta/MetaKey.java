package pl.edu.agh.ki.grieg.meta;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

/**
 * Value class representing metadata entry type (not specific, concrete entry,
 * but rather the general idea, i.e. "Composer [String]" rather than
 * "F. Chopin".
 * 
 * @author los
 * 
 * @param <T>
 *            Type of the data associated with the key
 */
public final class MetaKey<T> {

    /** Type of the metadata */
    public final Class<T> type;

    /** Name of the entry */
    public final String name;

    MetaKey(String name, Class<T> type) {
        checkNotNull(name, "Key name cannot be null");
        checkNotNull(type, "Key type cannot be null");
        this.name = name;
        this.type = type;
    }

    /**
     * Casts specified object into the type associated with this key.
     * 
     * @param o
     *            Object to cast
     * @return Object casted to the right type
     */
    public T cast(Object o) {
        return type.cast(o);
    }

    @Override
    public String toString() {
        return name + "[" + type.getSimpleName() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MetaKey<?>) {
            MetaKey<?> key = (MetaKey<?>) o;
            return type.equals(key.type) && name.equals(key.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, name);
    }

}
