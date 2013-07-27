package pl.edu.agh.ki.grieg.analysis;

import java.util.Arrays;

import pl.edu.agh.ki.grieg.util.Range;
import pl.edu.agh.ki.grieg.util.iteratee.AbstractEnumerator;
import pl.edu.agh.ki.grieg.util.iteratee.Enumeratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;

/**
 * Class grouping ("compressing") fixed-size portions of raw PCM data into
 * summarized amplitude ranges. For each portion accumulated from the data
 * received from the enumerator it is connected to, {@code WaveCompressor}
 * calculates the minimal and maximal amplitude for each channel and pushes
 * this data to its outputs.
 * 
 * @author los
 */
public class WaveCompressor extends AbstractEnumerator<Range[]> implements
        Enumeratee<float[][], Range[]> {

    /** Initial value of ranges */
    private static final Range INITIAL = new Range(1, -1);

    /** Number of channels of the input data */
    private final int channels;

    /** Number of samples to aggregate */
    private final int packetSize;

    /** Amplitude range for each channel */
    private Range[] ranges;

    /** Index (in the single packet) of the next sample to be processed */
    private int index = 0;

    /**
     * Creates new compressor for specified number of channel, aggregating
     * specified amount of samples per each output
     * 
     * @param channels
     *            Number of channels of the input data
     * @param packetSize
     *            How much samples should be processed before
     */
    public WaveCompressor(int channels, int packetSize) {
        this.channels = channels;
        this.packetSize = packetSize;
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
        final int chunkLength = item[0].length;
        for (int i = 0; i < chunkLength; ++i) {
            for (int j = 0; j < channels; ++j) {
                ranges[j] = ranges[j].extendWith(item[j][i]);
            }
            if (++index == packetSize) {
                index = 0;
                pushChunk(ranges);
                resetExtrema();
            }
        }
        return State.Cont;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finished() {
        if (index > 0) {
            pushChunk(ranges);
        }
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
