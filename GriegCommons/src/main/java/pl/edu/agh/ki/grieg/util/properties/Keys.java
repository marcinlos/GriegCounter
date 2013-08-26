package pl.edu.agh.ki.grieg.util.properties;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Static utilities for dealing with heterogenous map keys.
 *
 * @author los
 */
public final class Keys {

    private Keys() {
        // non-instantiable
    }

    /**
     * Creates new {@link Key} object.
     *
     * @param name
     *            Name of the entry
     * @param type
     *            Type of the assoaciated data
     * @return New key
     */
    public static <T> Key<T> make(String name, Class<T> type) {
        return new Key<T>(name, type);
    }

    /**
     * Creates a set of keys of unspecified bound, as turns out to be a little
     * bit awkward and uncomfortable.
     *
     * @param keys
     *            Keys to create set of
     * @return Set consisting of all the specified keys
     */
    public static Set<Key<?>> set(Key<?>... keys) {
        return Sets.<Key<?>> newHashSet(keys);
    }

}
