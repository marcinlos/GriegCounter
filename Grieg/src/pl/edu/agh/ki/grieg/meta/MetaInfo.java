package pl.edu.agh.ki.grieg.meta;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Maps;

public class MetaInfo {
    
    private final Map<String, Object> info = Maps.newHashMap();

    public MetaInfo() {
        // empty
    }
    
    public <T> Object put(MetaKey<T> key, T value) {
        return put(key.name, value);
    }
    
    public <T> Object put(String name, T value) {
        checkNotNull(value, "Null values not supported");
        return info.put(name, value);
    }
    
    public <T> T get(MetaKey<T> key) {
        return get(key.name, key.type);
    }
    
    public <T> T get(String name, Class<T> type) {
        Object o = info.get(name);
        return o == null ? null : type.cast(o);
    }
    
    public Set<String> getKeys() {
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

    public boolean contains(String key) {
        return info.containsKey(key);
    }

    public Object remove(String key) {
        return info.remove(key);
    }

}
