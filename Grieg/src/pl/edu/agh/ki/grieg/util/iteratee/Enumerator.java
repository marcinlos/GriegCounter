package pl.edu.agh.ki.grieg.util.iteratee;

/**
 * Producer of stream of {@code T} items.
 * 
 * Enumerator is abstraction of active data source, producing chunks of data and
 * pushing it to the {@linkplain Iteratee}s for processing. It is fully
 * autonomous, it pushes the data to the iteratees instead of the data being
 * pulled, hence it can be used to model asynchronous input.
 * 
 * @author los
 * 
 * @param <T>
 *            Type of data produced by the {@code Enumerator}
 */
public interface Enumerator<T> {

    /**
     * Adds {@code consumer} to the set of {@linkplain Iteratee}s consuming the
     * output of this enumerator.
     * 
     * @param consumer
     *            Data consumer
     * 
     * @see #disconnect(Iteratee)
     */
    void connect(Iteratee<? super T> consumer);

    /**
     * Removes the {@code consumer} from the set of {@linkplain Iteratee}s
     * consuming the output of this enumerator.
     * 
     * @param consumer
     *            Data consumer to be removed
     * 
     * @see #connect(Iteratee)
     */
    void disconnect(Iteratee<? super T> consumer);

}
