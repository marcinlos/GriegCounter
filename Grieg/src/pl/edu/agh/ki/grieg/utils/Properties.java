package pl.edu.agh.ki.grieg.utils;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Interface of a map container with string keys, providing a layer of
 * type-safety through the use of class token-based retrieval.
 * 
 * <p>
 * Neither {@code null} keys nor values are allowed.
 * 
 * @author los
 */
public interface Properties {

    /**
     * Inserts new value into the map. Both {@code key} and {@code value} must
     * be non-{@code null}. Previous value associated with {@code key.name} is
     * returned, or {@code null} if the map did not contain such key prior to
     * insertion.
     * 
     * @param key
     *            Key of the new entry
     * @param value
     *            Value to be associated with {@code key.name}
     * @return Previous value associated with {@code key.name}
     */
    <T> Object put(Key<T> key, T value);

    /**
     * Inserts new value into the map.
     * 
     * @param name
     *            Key of the new entry
     * @param value
     *            Value to be associated with thekey
     * @return Previous value associated with the key
     * @see #put(Key, T)
     */
    <T> Object put(String name, T value);

    /**
     * Retrieves value associated with the {@code key.name}.
     * <ul>
     * <li>If there is no such value, returns {@code null}
     * <li>If there is such value, but it has incorrect type (is not instance of
     * {@code key.type}), {@code ClassCastException} is thrown
     * <li>Otherwise, returns value casted to correct type
     * </ul>
     * 
     * @param key
     *            Key to retrieve value associated with
     * @return Value of the mapping
     */
    <T> T get(Key<T> key);

    /**
     * Retrieves value associated with the key. Works as {@link #get(Key)}.
     * 
     * @param name
     *            Key to retrieve value associated with
     * @param type
     *            Type of the value to be extracted
     * @return Value of the mapping
     */
    <T> T get(String name, Class<T> type);

    /**
     * Same as {@link #get(Key)} except that it if the type of the value doesn't
     * match, {@code null} is returned instead of throwing
     * {@code ClassCastException}
     * 
     * @param name
     *            Key to retrieve value associated with
     * @param type
     *            Type of the value to be extracted
     * @return Value of the mapping
     */
    <T> T tryGet(String name, Class<T> type);

    /**
     * Retrieves object associated with specified key without the need to
     * specify value type. Returns {@code null} if the key is not present in the
     * map.
     * 
     * @param name
     *            Key to retrieve value associated with
     * @return Value as a raw {@code Object}
     */
    Object get(String name);

    /**
     * Checks whether the map contains value for the specified key
     * <strong>and</strong> it is of the correct type.
     * 
     * @param key
     *            Key to be investigated
     * @return {@code true} if the key is present in the map and the value is of
     *         the specified type, {@code false} otherwise
     */
    <T> boolean contains(Key<T> key);

    /**
     * Checks whether the map contains value for the specified key.
     * 
     * @param key
     *            Key to be investigated
     * @return {@code true} if the map contains {@code key}, {@code false}
     *         otherwise
     */
    boolean contains(String key);

    /**
     * Removes value associated with specified string key, if this value is of
     * the correct type as specified by the key.
     * 
     * @param key
     *            Key of the entry to be removed
     * @return Value of the mapping before removal
     */
    <T> T remove(Key<T> key);

    /**
     * Removes value associated with specified string key. Returns this value,
     * or {@code null} if the map does not contain such key.
     * 
     * @param key
     *            Key of the entry to be removed
     * @return Value of the mapping before removal
     */
    Object remove(String key);

    /**
     * @return Set of keys
     */
    Set<String> keySet();

    /**
     * @return Set of (key, value) pairs in this map
     */
    Set<Entry<String, Object>> entrySet();

    /**
     * @return {@link Map} view of this {@code Properties}
     */
    Map<String, Object> asMap();

    /**
     * @return Number of elements in the map
     */
    int size();

    /**
     * @return {@code true} if there are no keys in the map, {@code false}
     *         otherwise
     */
    boolean isEmpty();

    /**
     * Adds all the (key, value) pairs from the specified map to this map. In
     * case of conflicts, i.e. when the key is mapped in both maps, new value is
     * preferred.
     * 
     * @param other
     *            Map to be added to this one
     * @return {@code this}
     */
    Properties addAll(Properties other);

}