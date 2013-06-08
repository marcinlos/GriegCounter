package pl.edu.agh.ki.grieg.utils.iteratee;

/**
 * Implementation of sequential composition of two enumeratees. Input stream is
 * pushed to the first (inner) enumeratee, and the outer enumeratee is connected
 * to it's output.
 * 
 * @author los
 * 
 * @param <S>
 *            Input type of the inner enumeratee
 * @param <T>
 *            Output type of the inner / input of the outer
 * @param <U>
 *            Output type of the outer enumeratee
 */
public class CompositeEnumeratee<S, T, U> implements Enumeratee<S, U> {

    private Enumeratee<T, U> outer;
    private Enumeratee<S, T> inner;

    /**
     * Creates new {@code Enumeratee} by connecting {@code outer} to the
     * {@code inner}.
     * 
     * @param outer
     *            Outer enumeratee - will produce final output
     * @param inner
     *            Inner enumeratee - will consume original input
     */
    public CompositeEnumeratee(Enumeratee<T, U> outer, Enumeratee<S, T> inner) {
        this.outer = outer;
        this.inner = inner;
        inner.connect(outer);
    }

    /**
     * {@inheritDoc}
     * 
     * Pushes the item to the inner iteratee.
     */
    @Override
    public State step(S item) {
        return inner.step(item);
    }

    /**
     * {@inheritDoc}
     * 
     * Connects to the outer enumeratee.
     */
    @Override
    public void connect(Iteratee<? super U> consumer) {
        outer.connect(consumer);
    }

    /**
     * {@inheritDoc}
     * 
     * Disconnects consumer from the outer enumeratee.
     */
    @Override
    public void disconnect(Iteratee<? super U> consumer) {
        outer.disconnect(consumer);
    }

    /**
     * {@inheritDoc}
     * 
     * Passes the exception to the inner enumeratee.
     */
    @Override
    public void failed(Throwable e) {
        inner.failed(e);
    }

    /**
     * {@inheritDoc}
     * 
     * Passes the information to the inner enumeratee.
     */
    @Override
    public void finished() {
        inner.finished();
    }

}
