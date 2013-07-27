package pl.edu.agh.ki.grieg.playback.output;

import java.io.IOException;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.util.iteratee.State;

/**
 * Partial implementation of {@link AudioOutput}, to facilitate creating new
 * service implementations.
 * 
 * @author los
 */
public abstract class AbstractAudioOutput implements AudioOutput {

    /** Format of the supported data */
    private final SoundFormat format;

    /**
     * Creates {@link AbstractAudioOutput} with specified input format
     * 
     * @param format
     *            Format of the output's input
     */
    public AbstractAudioOutput(SoundFormat format) {
        this.format = format;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State step(float[][] item) {
        write(item);
        return State.Cont;
    }

    /**
     * Closes the output and logs the exception if it is thrown
     */
    protected void closeQuietly() {
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * @return Sound format of the output's input (i.e. data that is pushed to
     *         this output)
     */
    protected SoundFormat format() {
        return format;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finished() {
        closeQuietly();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failed(Throwable e) {
        closeQuietly();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(float[][] data) {
        write(data, 0, data[0].length);
    }

}
