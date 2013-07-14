package pl.edu.agh.ki.grieg.decoder.spi;

import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.data.SourceDetails;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.io.AudioFile;
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

    AudioStream openStream(InputStream stream) throws DecodeException,
            IOException;

    boolean readable(InputStream stream) throws IOException;
    
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
