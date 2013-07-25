package pl.edu.agh.ki.grieg.utils;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Simple implementation of {@link Properties}, using {@link Map} of objects as
 * the underlying container.
 * 
 * @author los
 */
public class PropertyMap extends AbstractProperties {

    /** Underlying container */
    private final Map<String, Object> info = Maps.newHashMap();

    /**
     * Creates new empty {@code PropertyMap} object.
     */
    public PropertyMap() {
        // empty
    }

    /**
     * Initialized a {@code PropertyMap} object with arbitrary {@link Properties}.
     * 
     * @param other
     */
    public PropertyMap(Properties other) {
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
