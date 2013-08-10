package pl.edu.agh.ki.grieg.model.observables;

import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.collect.Sets;

public abstract class AbstractModel<T> implements Model<T> {

    private final Class<? extends T> dataType;
    
    private T data;
    
    private final Set<Listener<? super T>> listeners;
    
    public AbstractModel(Class<? extends T> dataType) {
        this.dataType = dataType;
        listeners = Sets.newCopyOnWriteArraySet();
    }

    @Override
    public void addListener(Listener<? super T> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener<? super T> listener) {
        listeners.remove(listener);
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public Class<? extends T> getDataType() {
        return dataType;
    }

    @Override
    public <S> Model<? extends S> getChild(String path, Class<S> type) {
        return null;
    }
    
    public void update() {
        Listeners.updateAll(listeners, data);
    }
    
    public void update(T newValue) {
        this.data = newValue;
        Listeners.updateAll(listeners, newValue);
    }

}
