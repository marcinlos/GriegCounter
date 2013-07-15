package pl.edu.agh.ki.grieg.io;

import java.io.IOException;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.utils.iteratee.AbstractEnumerator;

/**
 * Implementation of {@link SampleEnumerator} using {@link AudioStream} as the
 * source of data. Allows reading data in arbitrarily sized portions and
 * forwarding it to attached iteratees.
 * 
 * @author los
 */
public class StreamSampleEnumerator extends AbstractEnumerator<float[][]>
        implements SampleEnumerator {

    /** Source of audio data */
    private AudioStream stream;

    /** Buffer for raw float PCM samples */
    private float[][] buffer;

    /** Size of the buffer */
    private int size;

    private volatile boolean pause = false;

    /**
     * Creates new {@link StreamSampleEnumerator} using {@code stream} as the
     * data source and buffering data using {@code bufferSize}d buffers.
     * 
     * @param stream
     *            Input data
     * @param bufferSize
     *            Requested size of the buffer
     */
    public StreamSampleEnumerator(AudioStream stream, int bufferSize) {
        this.stream = stream;
        this.size = bufferSize;
        buffer = makeBuffer(size);
    }

    /**
     * Creates buffer of a given size.
     * 
     * @param bufferSize
     *            Size of the requested buffer
     * @return Newly allocated buffer
     */
    private float[][] makeBuffer(int bufferSize) {
        final int channels = getFormat().getChannels();
        return new float[channels][bufferSize];
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
        final int channels = getFormat().getChannels();
        for (int j = 0; j < channels; ++j) {
            System.arraycopy(src[j], 0, dst[j], 0, length);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SoundFormat getFormat() {
        return stream.getFormat();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws AudioException, IOException {
        int read;
        while ((read = stream.readSamples(buffer)) > 0) {
            if (read < size) {
                float[][] newBuf = makeBuffer(read);
                copyBuffer(buffer, newBuf, read);
                pushChunk(newBuf);
            } else {
                pushChunk(buffer);
            }
        }
        endOfStream();
        stream.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
        pause = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume() {
        pause = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        pause();
    }

}
