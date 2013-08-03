package pl.edu.agh.ki.grieg.chart;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import com.google.common.collect.Sets;

public class Serie<T> {

    private final Set<SerieListener<T>> listeners;

    protected T data;

    {
        listeners = Sets.newCopyOnWriteArraySet();
    }
    
    public static <T> Serie<T> of(T data) {
        return new Serie<T>(data);
    }

    public Serie(T data) {
        setData(data);
    }

    public void setData(T data) {
        this.data = checkNotNull(data);
    }

    public T getData() {
        return data;
    }

    public void addListener(SerieListener<T> listener) {
        listeners.add(checkNotNull(listener));
    }

    public void removeListener(SerieListener<T> listener) {
        listeners.remove(listener);
    }

    public void signalChange() {
        for (SerieListener<T> listener : listeners) {
            listener.updated(this);
        }
    }

}
