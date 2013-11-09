package pl.edu.agh.ki.grieg.processing.model;

import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.model.Models;
import pl.edu.agh.ki.grieg.model.SimpleModel;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;


/**
 * Simple wrapper for an iteratee, translating received chunks of data to model
 * updates.
 * 
 * @author los
 *
 * @param <T> Type of the data represented by this model
 */
public class IterateeWrapper<T> implements Iteratee<T> {

    private final SimpleModel<T> model;
    
    public IterateeWrapper(Class<? extends T> clazz) {
        this.model = Models.simple(clazz);
    }
    
    public static <T> IterateeWrapper<T> of(Class<? extends T> clazz) {
        return new IterateeWrapper<T>(clazz);
    }
    
    /**
     * @return Model representing received values
     */
    public Model<T> getModel() {
        return model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State step(T item) {
        model.update(item);
        return State.Cont;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finished() {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failed(Throwable e) {
        // empty
    }

}
