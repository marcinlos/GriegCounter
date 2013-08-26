package pl.edu.agh.ki.grieg.util.properties;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

/**
 * Value class representing {@link Properties} entry type (not specific,
 * concrete entry, but rather the general idea, i.e. "Composer [String]" rather
 * than "F. Chopin".
 * 
 * @author los
 * 
 * @param <T>
 *            Type of the data associated with the key
 */
public final class Key<T> {

	/** Type of the metadata */
	private final Class<T> type;

	/** Name of the entry */
	private final String name;

	/**
	 * Creates new key with specified name and element type.
	 * 
	 * @param name
	 *            Name of the element
	 * @param type
	 *            Type of the element
	 */
	Key(String name, Class<T> type) {
		this.name = checkNotNull(name, "Key name cannot be null");
		this.type = checkNotNull(type, "Key type cannot be null");
	}

	/**
	 * @return Type of the described element
	 */
	public Class<T> getType() {
		return type;
	}

	/**
	 * @return Name of the described element
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Key<?>) {
			Key<?> other = (Key<?>) o;
			return type.equals(other.type) && name.equals(other.name);
		} else {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(type, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("Key{%s: %s}", name, type.getSimpleName());
	}

}
