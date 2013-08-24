package pl.edu.agh.ki.grieg.analysis;

import pl.edu.agh.ki.grieg.util.iteratee.AbstractEnumeratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;

public class Segmenter extends AbstractEnumeratee<float[][], float[][]> {

    /** Number of channels of the input data */
    private final int channels;

    /** Number of samples in each output chunk */
    private final int packetSize;

    /** Buffered data */
    private float[][] buffer;

    /** Index (in the single packet) of the next sample to be processed */
    private int index = 0;

    /**
     * Creates new compressor for specified number of channel, gathering
     * specified amount of samples per each output
     * 
     * @param channels
     *            Number of channels of the input data
     * @param packetSize
     *            How much samples should be processed before
     */
    public Segmenter(int channels, int packetSize) {
        this.channels = channels;
        this.packetSize = packetSize;
        this.buffer = new float[channels][packetSize];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State step(float[][] item) {
        final int chunkLength = item[0].length;
        for (int i = 0; i < chunkLength; ++i) {
            for (int j = 0; j < channels; ++j) {
                buffer[j][index] = item[j][i];
            }
            if (++index == packetSize) {
                index = 0;
                pushChunk(buffer);
            }
        }
        return State.Cont;
    }

    /**
     * Auxilary function that copies buffer content between two arrays.
     * 
     * @param src
     *            Source of data
     * @param dst
     *            Destination of data
     * @param length
     *            Number of samples to copy
     */
    private void copyBuffer(float[][] src, float[][] dst, int length) {
        for (int j = 0; j < channels; ++j) {
            System.arraycopy(src[j], 0, dst[j], 0, length);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finished() {
        if (index > 0) {
            float[][] newBuffer = new float[channels][index];
            copyBuffer(buffer, newBuffer, index);
            pushChunk(newBuffer);
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