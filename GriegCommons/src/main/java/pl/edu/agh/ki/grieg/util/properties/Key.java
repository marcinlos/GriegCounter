package pl.edu.agh.ki.grieg.util.properties;

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
public final class Key<T> {

    /** Type of the metadata */
    public final Class<T> type;

    /** Name of the entry */
    public final String name;

    Key(String name, Class<T> type) {
        this.name = checkNotNull(name, "Key name cannot be null");
        this.type = checkNotNull(type, "Key type cannot be null");
    }

    @Override
    public String toString() {
        return String.format("Key{%s: %s}", name, type.getSimpleName());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Key<?>) {
            Key<?> key = (Key<?>) o;
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
