package pl.edu.agh.ki.grieg.io;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    /** States for FSM this class fundamentally is */
    private enum PlaybackState {
        RUNNING, PAUSED, STOPPED
    }

    /** Current state of playback */
    private PlaybackState state = PlaybackState.RUNNING;

    /** Lock protecting playback state */
    private Lock lock = new ReentrantLock();

    /** Signaled upon state changes */
    private Condition stateChange = lock.newCondition();

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
        try {
            while (!isStopped()) {
                playSome();
            }
            endOfStream();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (AudioException e) {
            failure(e);
            throw e;
        } catch (IOException e) {
            failure(e);
            throw e;
        } finally {
            stream.close();
        }
    }

    /**
     * Process next chunk of data, possibly waiting for it if the playback is
     * paused.
     * 
     * @throws AudioException
     *             If there is an audio-related error
     * @throws IOException
     *             If an IO error occurs
     * @throws InterruptedException
     *             If the thread is interrupted
     */
    private void playSome() throws AudioException, IOException,
            InterruptedException {
        lock.lock();
        try {
            if (isRunning()) {
                proceed();
            } else if (isPaused()) {
                // wait for either resume or stop event
                while (isPaused()) {
                    stateChange.await();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Reads some audio data from the stream and pushes it to connected outputs.
     * 
     * @throws AudioException
     *             If there is a problem related to audio processing
     * @throws IOException
     *             If an IO error occured
     */
    private void proceed() throws AudioException, IOException {
        int read = stream.readSamples(buffer);
        if (read > 0) {
            float[][] data;
            if (read < size) {
                float[][] newBuf = makeBuffer(read);
                copyBuffer(buffer, newBuf, read);
                data = newBuf;
            } else {
                data = buffer;
            }
            pushChunk(data);
        } else {
            state = PlaybackState.STOPPED;
        }
    }

    /**
     * @return {@code true} if state is {@link PlaybackState#RUNNING}
     */
    private boolean isRunning() {
        return state == PlaybackState.RUNNING;
    }

    /**
     * @return {@code true} if state is {@link PlaybackState#STOPPED}
     */
    private boolean isStopped() {
        return state == PlaybackState.STOPPED;
    }

    /**
     * @return {@code true} if state is {@link PlaybackState#PAUSED}
     */
    private boolean isPaused() {
        return state == PlaybackState.PAUSED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
        lock.lock();
        state = PlaybackState.PAUSED;
        lock.unlock();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume() {
        changeAndSignal(PlaybackState.RUNNING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        changeAndSignal(PlaybackState.STOPPED);
    }

    /**
     * Changes state to a specified value and signals the associated condition
     * variable.
     * 
     * @param newState
     *            New playback state
     */
    private void changeAndSignal(PlaybackState newState) {
        lock.lock();
        try {
            state = newState;
            stateChange.signal();
        } finally {
            lock.unlock();
        }
    }

}
