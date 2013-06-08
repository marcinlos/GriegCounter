package pl.edu.agh.ki.grieg.decoder.spi;

import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.data.SourceDetails;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.io.AudioFile;

/**
 * Provider interface of audio file format(s) parser.
 * 
 * @author los
 */
public interface AudioFileParser {

    /**
     * 
     * @return Sequence of extensions commonly used by the supported formats
     */
    Iterable<String> extensions();

    /**
     * Extrcts information about the audio characteristics from the input
     * stream. This usually entails reading some data from the stream, which may
     * not be desirable for the implementation client. If that is the case, the
     * parser shold mark the position of the stream (using
     * {@linkplain InputStream#mark(int)}), read required amount of data and
     * reseting the position ({@linkplain InputStream#reset()}) afterwards.
     * 
     * @param stream
     *            Input stream
     * @return {@code SourceDetails} containing information about the input
     * 
     * @throws DecodeException
     *             If decoding failed
     * @throws IOException
     *             If an IO error occured
     */
    SourceDetails getDetails(InputStream stream) throws DecodeException,
            IOException;

    /**
     * Begins parsing of an input stream, extracting format information,
     * metadata and constructing audio decoder, capable of producing samples.
     * 
     * @param stream
     *            Input stream
     * @return {@code AudioFile} providing information about the file and an
     *         {@code AudioStream} implementation
     * @throws DecodeException
     *             If decoding failed
     * @throws IOException
     *             If an IO error occured
     */
    AudioFile open(InputStream stream) throws DecodeException, IOException;

}
