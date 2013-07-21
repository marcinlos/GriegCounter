package pl.edu.agh.ki.grieg.playback.output;

import java.io.Closeable;

import pl.edu.agh.ki.grieg.utils.iteratee.Iteratee;

public interface AudioOutput extends Closeable, Iteratee<float[][]> {

    /**
     * Starts playback using available data. Does not block.
     */
    public void start();

    /**
     * Sends PCM data to the output represented by this object.
     * 
     * @param data
     *            Floating-point PCM-encoded audio data
     */
    public void write(float[][] data);

    /**
     * Sends PCM data to the output represented by this object.
     * 
     * @param data
     *            Buffer containing floating-point PCM-encoded audio data
     * @param offset
     *            Offset of the beginning of the data that should be pushed to
     *            the output
     * @param length
     *            Amount of samples to be pushed
     */
    public void write(float[][] data, int offset, int length);
}
