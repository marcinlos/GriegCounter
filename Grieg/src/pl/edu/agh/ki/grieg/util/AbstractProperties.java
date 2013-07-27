package pl.edu.agh.ki.grieg.util;

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
    public void putByte(String name, byte value) {
        put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putChar(String name, char value) {
        put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putShort(String name, short value) {
        put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putInt(String name, int value) {
        put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putLong(String name, long value) {
        put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putFloat(String name, float value) {
        put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putDouble(String name, double value) {
        put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putString(String name, String value) {
        put(name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(String name) {
        return get(name, byte.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char getChar(String name) {
        return get(name, char.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getShort(String name) {
        return get(name, short.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(String name) {
        return get(name, int.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(String name) {
        return get(name, long.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(String name) {
        return get(name, float.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(String name) {
        return get(name, double.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String name) {
        return get(name, String.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getByte(String name, byte def) {
        return contains(name) ? getByte(name) : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char getChar(String name, char def) {
        return contains(name) ? getChar(name) : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getShort(String name, short def) {
        return contains(name) ? getShort(name) : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInt(String name, int def) {
        return contains(name) ? getInt(name) : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(String name, long def) {
        return contains(name) ? getLong(name) : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getFloat(String name, float def) {
        return contains(name) ? getFloat(name) : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDouble(String name, double def) {
        return contains(name) ? getDouble(name) : def;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String name, String def) {
        return contains(name) ? getString(name) : def;
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
