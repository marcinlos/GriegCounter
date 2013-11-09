package pl.edu.agh.ki.grieg.analysis;

import pl.edu.agh.ki.grieg.util.iteratee.AbstractEnumeratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;

/**
 * Enumeratee choosing one of the input channels and forwarding it.
 * 
 * @author los
 */
public class ChannelMultiplexer extends AbstractEnumeratee<float[][], float[]> {

    /** Which channel to forward */
    private final int n;

    public ChannelMultiplexer(int n) {
        this.n = n;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State step(float[][] item) {
        pushChunk(item[n]);
        return State.Cont;
    }

}
