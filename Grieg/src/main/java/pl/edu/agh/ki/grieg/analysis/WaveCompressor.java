package pl.edu.agh.ki.grieg.analysis;

import java.util.Arrays;

import pl.edu.agh.ki.grieg.util.Range;
import pl.edu.agh.ki.grieg.util.iteratee.AbstractEnumeratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;

/**
 * Class grouping ("compressing") incomint portions of raw PCM data into
 * summarized amplitude ranges. For each portion {@code WaveCompressor}
 * calculates the minimal and maximal amplitude for each channel and pushes
 * this data to its outputs.
 * 
 * @author los
 */
public class WaveCompressor extends AbstractEnumeratee<float[][], Range[]> {

    /** Initial value of ranges */
    private static final Range INITIAL = new Range(1, -1);

    /** Number of channels of the input data */
    private final int channels;

    /** Amplitude range for each channel */
    private Range[] ranges;

    /**
     * Creates new compressor for specified number of channel, aggregating
     * specified amount of samples per each output
     * 
     * @param channels
     *            Number of channels of the input data
     */
    public WaveCompressor(int channels) {
        this.channels = channels;
        this.ranges = new Range[channels];
        resetExtrema();
    }

    /**
     * Restores initial state of range arrays
     */
    private void resetExtrema() {
        Arrays.fill(ranges, INITIAL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State step(float[][] item) {
        resetExtrema();
        final int chunkLength = item[0].length;
        for (int i = 0; i < chunkLength; ++i) {
            for (int j = 0; j < channels; ++j) {
                ranges[j] = ranges[j].extendWith(item[j][i]);
            }
        }
        pushChunk(ranges);
        resetExtrema();
        return State.Cont;
    }

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
