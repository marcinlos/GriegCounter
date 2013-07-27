package pl.edu.agh.ki.grieg.util.iteratee;

/**
 * Iteratee consuming the stream of {@code T} items.
 * 
 * <p>
 * Iteratee is an abstraction of the concept of processing chunks of data, which
 * is pushed to it by the {@linkplain Enumerator}. It is passive, it has no way
 * to pull the data from the {@code Enumerator}.
 * 
 * @author los
 * 
 * @param <T>
 *            Type of elements processed by the {@code Iteratee}
 */
public interface Iteratee<T> {

    /**
     * Push the data to the iteratee and have it processed. If this method
     * returns {@link State#Done}, successive calls should throw
     * {@link IterateeDoneException}.
     * 
     * @param item
     *            Data chunk to be processed
     * 
     * @throws IterateeDoneException
     *             If it's invoked after the iteratee has finished processing
     *             input.
     * 
     * @return New state of the iteratee
     */
    State step(T item);

    /**
     * Inform the iteratee about the end of a stream.
     * 
     * @throws IterateeDoneException
     *             If it's invoked after the iteratee has finished processing
     *             input.
     */
    void finished();

    /**
     * Inform the iteratee about an exception during producing.
     * 
     * @param e
     *            Exception that occured during production
     */
    void failed(Throwable e);

}
