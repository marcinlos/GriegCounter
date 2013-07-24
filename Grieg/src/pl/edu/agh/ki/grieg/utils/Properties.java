package pl.edu.agh.ki.grieg.utils;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Simple implementation of {@link TypedMap}, using {@link Map} of objects as
 * the underlying container.
 * 
 * @author los
 */
public class Properties extends AbstractTypedMap {

    /** Underlying container */
    private final Map<String, Object> info = Maps.newHashMap();

    /**
     * Creates new empty {@code Properties} object.
     */
    public Properties() {
        // empty
    }

    /**
     * Initialized a {@code Properties} object with arbitrary {@link TypedMap}.
     * 
     * @param other
     */
    public Properties(TypedMap other) {
        addAll(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> asMap() {
        return info;
    }

}
