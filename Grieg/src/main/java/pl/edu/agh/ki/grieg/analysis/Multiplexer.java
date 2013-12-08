package pl.edu.agh.ki.grieg.analysis;

import pl.edu.agh.ki.grieg.util.iteratee.AbstractEnumeratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;

/**
 * Enumeratee choosing one of the input channels and forwarding it.
 * 
 * @author los
 */
public class Multiplexer<T> extends AbstractEnumeratee<T[], T> {

    /** Which channel to forward */
    private final int n;

    public Multiplexer(int n) {
        this.n = n;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State step(T[] item) {
        pushChunk(item[n]);
        return State.Cont;
    }
    
    public static <T> Multiplexer<T> choose(int n) {
        return new Multiplexer<T>(n);
    }

}
