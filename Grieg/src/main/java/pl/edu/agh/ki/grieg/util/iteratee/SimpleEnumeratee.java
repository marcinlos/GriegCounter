package pl.edu.agh.ki.grieg.util.iteratee;

/**
 * {@code Enumeratee} implementation transforming the input stream with a
 * {@linkplain Function}.
 * 
 * @author los
 * 
 * @param <S>
 *            Type of an input stream
 * @param <T>
 *            Type of an output stream
 */
public class SimpleEnumeratee<S, T> extends AbstractEnumerator<T> implements
        Enumeratee<S, T> {

    private Function<S, T> transform;

    /**
     * Creates an {@code Enumerator} transforming the input stream using
     * {@code transform} and pushing the results into the output stream.
     * 
     * @param transform
     *            Function used to transform chunks
     */
    public SimpleEnumeratee(Function<S, T> transform) {
        this.transform = transform;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State step(S item) {
        try {
            T value = transform.apply(item);
            pushChunk(value);
            return State.Cont;
        } catch (Throwable e) {
            signalFailure(e);
            return State.Done;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failed(Throwable e) {
        signalFailure(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finished() {
        signalEndOfStream();
    }

}
