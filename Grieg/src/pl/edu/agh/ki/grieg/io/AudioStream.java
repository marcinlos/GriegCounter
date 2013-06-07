package pl.edu.agh.ki.grieg.io;

import java.io.Closeable;

import pl.edu.agh.ki.grieg.data.Format;

/**
 * Stream of raw, decoded audio data.
 * 
 * @author los
 */
public interface AudioStream extends Closeable {

    /**
     * Decodes part of the input stream and writes decoded samples to
     * {@code buffer}.
     * 
     * @param bffer
     *            Buffer to which the samples shall be written
     * 
     * @throws AudioException
     *             In case of an error during reading input
     */
    void readSamples(float[][] buffer) throws AudioException;

    /**
     * @return {@link Format} structure describing the audio parameters
     */
    Format getFormat();

}
