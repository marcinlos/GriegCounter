package pl.edu.agh.ki.grieg.model.observables;

public interface Listener<T> {

    void update(T data);
    
}
