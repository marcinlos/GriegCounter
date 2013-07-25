package pl.edu.agh.ki.grieg.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

/**
 * Abstract implementation of the {@link Properties}, requiring the implementor
 * to provide modifiable {@link Map} view of the mapping.
 * 
 * @author los
 */
public abstract class AbstractProperties implements Properties {

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Object put(Key<T> key, T value) {
        return put(key.name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Object put(String name, T value) {
        checkNotNull(name);
        checkNotNull(value, "Null values not supported");
        return asMap().put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T get(Key<T> key) {
        return get(key.name, key.type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T get(String name, Class<T> type) {
        Object o = get(name);
        return type.cast(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T tryGet(String name, Class<T> type) {
        Object o = get(name);
        return type.isInstance(o) ? type.cast(o) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String name) {
        return asMap().get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean contains(Key<T> key) {
        Object o = get(key.name);
        return key.type.isInstance(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(String key) {
        return asMap().containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object remove(String key) {
        return asMap().remove(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T remove(Key<T> key) {
        T value = get(key);
        remove(key.name);
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> keySet() {
        return asMap().keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return asMap().entrySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return asMap().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return asMap().isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Properties addAll(Properties other) {
        asMap().putAll(other.asMap());
        return this;
    }

}
