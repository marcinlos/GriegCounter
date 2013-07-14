package pl.edu.agh.ki.grieg.io;

import java.io.Closeable;
import java.io.IOException;

import pl.edu.agh.ki.grieg.data.SoundFormat;

/**
 * Stream of raw, decoded audio data.
 * 
 * @author los
 */
public interface AudioStream extends Closeable {

    /**
     * Decodes part of the input stream and writes decoded samples to
     * {@code buffer}. Size of an outer array shold be nonzero. If it's greater
     * than the {@code getFormat().channels} (the number of channels in the
     * stream), the remaining portion of the array will be left unchanged. If
     * it's smaller, the rest of the channels' samples will be discarded.
     * Returns number of read samples, which is in the range
     * 
     * <pre>
     * 0 &lt;= samplesRead &lt;= bufSize
     * </pre>
     * 
     * where {@code bufSize} is {@code buffer[0].length}
     * 
     * @param bffer
     *            Buffer to which the samples shall be written
     * 
     * @return Number of samples actually read..
     * 
     * @throws AudioException
     *             In case of an error during reading input
     * @throws IOException
     *             In case of a plain IO error
     */
    int readSamples(float[][] buffer) throws AudioException, IOException;

    /**
     * @return {@linkplain SoundFormat} structure describing the audio
     *         parameters
     */
    SoundFormat getFormat();

}
