package pl.edu.agh.ki.grieg.util.iteratee;

/**
 * Abstract implementation of {@link Enumeratee} interace, based on
 * {@link AbstractEnumerator} and {@link AbstractIteratee}.
 * 
 * @author los
 * 
 * @param <S>
 *            Type of consumed items (input stream)
 * @param <T>
 *            Type of produced items (output stream)
 */
public abstract class AbstractEnumeratee<S, T> extends AbstractEnumerator<T>
        implements Enumeratee<S, T> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void finished() {
        signalEndOfStream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failed(Throwable e) {
        signalFailure(e);
    }

}
