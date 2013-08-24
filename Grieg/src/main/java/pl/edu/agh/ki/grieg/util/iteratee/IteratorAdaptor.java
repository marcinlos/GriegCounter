package pl.edu.agh.ki.grieg.util.iteratee;

import java.util.Iterator;

/**
 * {@linkplain Enumerator} implementation wrapping {@linkplain Iterator}.
 * 
 * <p>
 * In the constructor this class accepts an {@code Iterator}, which is later
 * repeatedly called to produce a stream of {@code T} elements. Exceptions
 * thrown by the underlying {@link Iterator#next()} calls are forwarded to the
 * connected iteratees.
 * 
 * @author los
 * 
 * @param <T>
 *            Type of produced elements
 */
public class IteratorAdaptor<T> extends AbstractEnumerator<T> implements
        AutoEnumerator<T> {

    /** Actual item source */
    private final Iterator<? extends T> iterator;

    /**
     * Creates new {@code IteratorAdaptor} wrapping the {@code iterator}.
     * 
     * @param iterator
     *            Stream generator
     */
    public IteratorAdaptor(Iterator<? extends T> iterator) {
        this.iterator = iterator;
    }

    /**
     * Repeatedly calls the underlying iterator, thus producing the stream of
     * elements, which are pushed to the connected iteratees.
     */
    @Override
    public void run() {
        try {
            while (iterator.hasNext()) {
                pushChunk(iterator.next());
            }
            signalEndOfStream();
        } catch (Throwable e) {
            signalFailure(e);
        }
    }

}
