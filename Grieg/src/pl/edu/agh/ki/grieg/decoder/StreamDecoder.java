package pl.edu.agh.ki.grieg.decoder;

import pl.edu.agh.ki.grieg.io.AudioStream;

/**
 * Interface of a stream decoder, producing raw audio data directly from the
 * input byte stream.
 * 
 * Specification of {@linkplain AudioStream} interface, differing by the
 * exception specification of the {@link #readSamples(float[][])} method.
 * 
 * @author los
 */
public interface StreamDecoder extends AudioStream {

    /**
     * {@inheritDoc}
     * 
     * @throws DecodeException
     *             In case of an error during decoding
     */
    void readSamples(float[][] buffer) throws DecodeException;

}
