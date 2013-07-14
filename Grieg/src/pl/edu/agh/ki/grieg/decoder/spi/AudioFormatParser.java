package pl.edu.agh.ki.grieg.decoder.spi;

import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.io.AudioStream;

/**
 * Provider interface of audio file format(s) parser.
 * 
 * @author los
 */
public interface AudioFormatParser {

    /**
     * 
     * @return Sequence of extensions commonly used by the supported formats
     */
    Iterable<String> extensions();

    /**
     * Opens a stream of audio data.
     * 
     * @param stream
     *            Input data
     * @return {@code AudioStream} allowing to read raw PCM data
     * @throws DecodeException
     *             If decoding failed
     * @throws IOException
     *             If an IO error occurs
     */
    AudioStream openStream(InputStream stream) throws DecodeException,
            IOException;

    /**
     * Checks whether the data in the stream is supported by this parser.
     * 
     * @param stream
     *            Input data
     * @return {@code true} if the parser supports format of the input,
     *         {@code false} otherwise
     * @throws IOException
     *             If an IO error occurs
     */
    boolean readable(InputStream stream) throws IOException;

}
