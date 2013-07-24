package pl.edu.agh.ki.grieg.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Maps;

public class Properties {
    
    private final Map<String, Object> info = Maps.newHashMap();
    
    public Properties() {
        // empty
    }
    
    public Properties(Properties other) {
        addAll(other);
    }

    public <T> Object put(Key<T> key, T value) {
        return put(key.name, value);
    }
    
    public <T> Object put(String name, T value) {
        checkNotNull(name);
        checkNotNull(value, "Null values not supported");
        return info.put(name, value);
    }
    
    public <T> T get(Key<T> key) {
        return get(key.name, key.type);
    }
    
    public <T> T get(String name, Class<T> type) {
        Object o = get(name);
        return type.cast(o);
    }
    
    public <T> T tryGet(String name, Class<T> type) {
        Object o = get(name);
        return type.isInstance(o) ? type.cast(o) : null;
    }
    
    public Object get(String name) {
        return info.get(name);
    }
    
    public <T> boolean contains(Key<T> key) {
        Object o = info.get(key.name);
        return key.type.isInstance(o);
    }

    public boolean contains(String key) {
        return info.containsKey(key);
    }

    public Object remove(String key) {
        return info.remove(key);
    }
    
    public <T> T remove(Key<T> key) {
        T value = get(key);
        remove(key.name);
        return value;
    }
    
    public Set<String> keySet() {
        return info.keySet();
    }

    public Set<Entry<String, Object>> entrySet() {
        return info.entrySet();
    }
    
    public int size() {
        return info.size();
    }

    public boolean isEmpty() {
        return info.isEmpty();
    }
    
    public Properties addAll(Properties other) {
        info.putAll(other.info);
        return this;
    }

}
