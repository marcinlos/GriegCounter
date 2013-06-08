package pl.edu.agh.ki.grieg.utils.iteratee;

/**
 * Abstract {@linkplain Iteratee} implementation.
 * 
 * <p>
 * Although it does not implement any method from {@linkplain Iteratee}
 * interface, it keeps track of iteratee's state (whether it has finished
 * processing).
 * 
 * @author los
 * 
 * @param <T>
 */
public abstract class AbstractIteratee<T> implements Iteratee<T> {

    private boolean done = false;

    /**
     * If the iteratee has previously finished processing (i.e. the
     * {@linkplain #done()} method has been called), it throws
     * {@linkplain IterateeDoneException}. It's designed to be called at the
     * beginning of {@linkplain #step} method, to ensure contract described in
     * that method's documentation.
     */
    protected void checkState() {
        if (isDone()) {
            throw new IterateeDoneException();
        }
    }

    /**
     * @return {@code true} if the iteratee has finished processign,
     *         {@code false} otherwise
     */
    protected boolean isDone() {
        return done;
    }

    /**
     * Sets the internal variable indicating that the iteratee has finished
     * input processing.
     */
    protected void done() {
        done = true;
    }
}
