package pl.edu.agh.ki.grieg.utils.iteratee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * Concrete implementation of the {@linkplain Enumerator}, managing connected
 * {@linkplain Iteratee}s.
 * 
 * <p>
 * {@code AbstractEnumerator} maintains a collection of {@code Iteratee}s
 * connected to the output stream. {@code Iteratee}s that return
 * {@code State.Done} are removed from this collection, so that general-purpose
 * {@linkplain Enumerator}s deriving from {@code AbstractEnumerator} need not be
 * concerned with output management and just produce the values by calling
 * {@link #pushChunk}.
 * 
 * @author los
 * 
 * @param <T>
 *            Type of produced values
 */
public abstract class AbstractEnumerator<T> implements Enumerator<T> {

    protected Collection<Iteratee<? super T>> outputs;

    /**
     * Produces a new {@code AbstractEnumeratee}.
     */
    public AbstractEnumerator() {
        initOutputs();
    }

    /**
     * Creates a container for output {@code Iteratee}s.
     * 
     * <p>
     * By default it creates an {@linkplain ArrayList} of {@code Iteratee}s.
     * Override it to customize the output listeners collection.
     */
    protected void initOutputs() {
        outputs = new ArrayList<Iteratee<? super T>>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(Iteratee<? super T> consumer) {
        outputs.add(consumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect(Iteratee<? super T> consumer) {
        outputs.remove(consumer);
    }

    /**
     * Gives access to the connected iteratees. Returned collection is
     * unmodifiable.
     * 
     * @return Collection of iteratees consuming the enumerator's output
     */
    public Collection<Iteratee<? super T>> getIteratees() {
        return Collections.unmodifiableCollection(outputs);
    }

    /**
     * Pushes the {@code value} to the connected iteratees. Removes iteratees if
     * {@linkplain Iteratee#step} returns {@link State#Done}.
     * 
     * @param value
     *            Next stream chunk to be consumed by the connected iteratees
     */
    public void pushChunk(T value) {
        for (Iterator<Iteratee<? super T>> it = outputs.iterator(); it
                .hasNext();) {
            Iteratee<? super T> iteratee = it.next();
            if (iteratee.step(value) == State.Done) {
                it.remove();
            }
        }
    }

    /**
     * Propagates error to the output iteratees.
     * 
     * @param e
     *            {@code Throwable} that describes the error
     */
    public void failure(Throwable e) {
        for (Iteratee<?> it : outputs) {
            it.failed(e);
        }
    }

    /**
     * Notifies output iteratees about the end of the input, invoking
     * {@linkplain Iteratee#finished()} method for each connected
     * {@code Iteratee}.
     */
    public void endOfStream() {
        for (Iteratee<?> it : outputs) {
            it.finished();
        }
    }

}
