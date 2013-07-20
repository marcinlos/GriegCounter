package pl.edu.agh.ki.grieg.analysis;

import pl.edu.agh.ki.grieg.utils.iteratee.AbstractIteratee;
import pl.edu.agh.ki.grieg.utils.iteratee.State;

/**
 * Simple class counting the received samples.
 * 
 * @author los
 */
public class SampleCounter extends AbstractIteratee<float[][]> {

    /** Amount of samples received so far */ 
    private int sampleCount = 0;

    /**
     * {@inheritDoc}
     */
    @Override
    public State step(float[][] item) {
        sampleCount += item[0].length;
        return State.Cont;
    }

    /**
     * @return Amount of frames received so far
     */
    public int getFrameCount() {
        return sampleCount;
    }

}
