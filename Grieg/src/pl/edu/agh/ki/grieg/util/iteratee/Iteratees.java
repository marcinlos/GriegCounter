package pl.edu.agh.ki.grieg.util.iteratee;

import java.util.Iterator;

/**
 * Static helper class containing iteratee combinators and other convenience
 * methods.
 * 
 * @author los
 */
public class Iteratees {

    private Iteratees() {
        // non-instantiable
    }

    /**
     * Produces a new {@code Enumeratee} consuming {@code S} stream and
     * producing {@code U} stream by composing two {@linkplain Enumeratee}s,
     * i.e. connecting {@code inner}'s output and {@code outer}'s input.
     * 
     * @param outer
     *            Outer {@code Enumeratee}, final producer
     * @param inner
     *            Inner {@code Enumeratee}, initial consumer
     * @return {@code Enumeratee} combining the {@code inner} and {@code outer}
     * @see CompositeEnumeratee
     */
    public static <S, T, U> Enumeratee<S, U> compose(Enumeratee<T, U> outer,
            Enumeratee<S, T> inner) {
        return new CompositeEnumeratee<S, T, U>(outer, inner);
    }

    /**
     * Produces new {@code Enumeratee} consuming {@code S} stream, passing the
     * values to the {@code transform} and pushing the results to the output
     * stream.
     * 
     * @param transform
     *            Function used to transform stream chunks
     * @return {@code Enumeratee} transforming the input stream
     * @see SimpleEnumeratee
     */
    public static <S, T> Enumeratee<S, T> mapper(Function<S, T> transform) {
        return new SimpleEnumeratee<S, T>(transform);
    }

    /**
     * Creates an {@code Enumerator} using the stream produced by the {@code it}
     * iterator.
     * 
     * @param it
     *            Iterator producing a stream
     * @return {@code Enumerator} forwarding the stream produced by {@code it}
     */
    public static <T> AutoEnumerator<T> enumerator(Iterator<? extends T> it) {
        return new IteratorAdaptor<T>(it);
    }

    /**
     * @return Universal forwarding enumeratee
     */
    @SuppressWarnings("unchecked")
    public static <T> Enumeratee<T, T> forwarder() {
        return (Enumeratee<T, T>) Forwarder.INSTANCE;
    }

    /**
     * Creates an {@code Enumerator} using the stream extracted from the
     * {@code it} iterable.
     * 
     * @param it
     *            {@code Iterable} containing the stream
     * @return {@code Enumerator} forwarding the stream produced by {@code it}
     */
    public static <T> AutoEnumerator<T> enumerator(Iterable<? extends T> it) {
        return enumerator(it.iterator());
    }

    @SuppressWarnings("unchecked")
    public static <T> Iteratee<T> upcast(Iteratee<? super T> sink) {
        return (Iteratee<T>) sink;
    }

    @SuppressWarnings("unchecked")
    public static <T> Enumerator<T> upcast(Enumerator<? extends T> source) {
        return (Enumerator<T>) source;
    }

    @SuppressWarnings("unchecked")
    public static <S, T> Enumeratee<S, T> upcast(
            Enumeratee<? super S, ? extends T> transform) {
        return (Enumeratee<S, T>) transform;
    }

    /**
     * Implementation of an universal forwarder enumeratee, that transparently
     * sends chunks and other signals to attached iteratees.
     */
    private static final class Forwarder extends AbstractEnumerator<Object>
            implements Enumeratee<Object, Object> {

        /** Single instance */
        public static final Forwarder INSTANCE = new Forwarder();

        private Forwarder() {
            // non-instantiable outside
        }

        @Override
        public State step(Object item) {
            pushChunk(item);
            return State.Cont;
        }

        @Override
        public void finished() {
            signalEndOfStream();
        }

        @Override
        public void failed(Throwable e) {
            signalFailure(e);
        }

    }

}
