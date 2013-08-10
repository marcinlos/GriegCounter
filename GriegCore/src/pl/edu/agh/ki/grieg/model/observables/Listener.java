package pl.edu.agh.ki.grieg.model.observables;

/**
 * Interface of the generic listener receiving notifications about updates of
 * the model.
 * 
 * @author los
 * 
 * @param <T>
 *            Type of the data contained in observed model
 */
public interface Listener<T> {

    /**
     * Invoked by the model when the model is updated. This usually means change
     * of the data, though it is not required that the new value is different
     * from the old.
     * 
     * @param data
     *            New value of the observed model's data
     */
    void update(T data);

}
