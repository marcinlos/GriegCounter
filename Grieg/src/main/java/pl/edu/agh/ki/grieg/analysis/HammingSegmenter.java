package pl.edu.agh.ki.grieg.analysis;

import pl.edu.agh.ki.grieg.util.iteratee.AbstractEnumeratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;

/**
 * Partitions incoming audio data into overlapping chunks of fixed size, and
 * applies window function to each chunk to provide data suitable for FFT.
 * 
 * TODO: Naive, inefficient implementation. It can be done much better!
 * 
 * @author los
 */
public class HammingSegmenter extends AbstractEnumeratee<float[][], float[][]> {

    /** Values of window function for each sample in the chunk */
    private final float[] window;

    /** Buffer for incoming data */
    private final float[][] buffer;

    /** Output buffer, created when there is enough data accumulated */
    private final float[][] output;

    /** Size of each chunk */
    private final int bufferSize;

    /** Difference between succesive chunks' first samples */
    private final int hopSize;

    /** Number of channels */
    private final int channels;

    /** Where to store next sample */
    private int nextIndex = 0;

    /**
     * Creates the HammingSegmenter suitable for processing specified amout of
     * channels, using provided parameters.
     * 
     * @param channels
     *            Number of channels
     * @param hopSize
     *            Step between succesive chunks
     * @param bufferSize
     *            Size of each chunk
     */
    public HammingSegmenter(int channels, int hopSize, int bufferSize) {
        this.bufferSize = bufferSize;
        this.hopSize = hopSize;
        this.channels = channels;
        this.window = createHammingWindow(bufferSize);
        this.buffer = new float[channels][bufferSize];
        this.output = new float[channels][bufferSize];
    }

    /**
     * Taken from Beads, pretty strange. This doesn't seem to be a real hamming
     * window. Requires further investigation.
     * 
     * @param bufferSize
     *            Size of the whole chunk (not hop size)
     * @return Hamming window of required resolution
     */
    private float[] createHammingWindow(int bufferSize) {
        float[] b = new float[bufferSize];
        int lower = bufferSize / 4;
        int upper = bufferSize - lower;
        for (int i = 0; i < bufferSize; i++) {
            if (i < lower || i > upper) {
                float x = (float) i / (float) (bufferSize - 1);
                double arg = Math.PI + Math.PI * 4.0f * x;
                b[i] = 0.5f * (1.0f + (float) Math.cos(arg));
            } else {
                b[i] = 1.0f;
            }
        }
        return b;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State step(float[][] item) {
        final int chunkSize = item[0].length;
        for (int i = 0; i < chunkSize; ++i) {
            for (int j = 0; j < channels; ++j) {
                buffer[j][nextIndex] = item[j][i];
            }
            ++nextIndex;
            if (nextIndex == bufferSize) {
                nextIndex -= hopSize;
                prepareOutput();
                pushChunk(output);
                shift();
            }
        }
        return State.Cont;
    }

    /**
     * Moved audio data "back" inside the buffer
     */
    private void shift() {
        final int rest = bufferSize - hopSize;
        for (float[] channel : buffer) {
            System.arraycopy(channel, hopSize, channel, 0, rest);
        }
    }

    /**
     * Computes output buffer
     */
    private void prepareOutput() {
        for (int i = 0; i < channels; ++i) {
            for (int j = 0; j < bufferSize; ++j) {
                output[i][j] = window[j] * buffer[i][j];
            }
        }
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
