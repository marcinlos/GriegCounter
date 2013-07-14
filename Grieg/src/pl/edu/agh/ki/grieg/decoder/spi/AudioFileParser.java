package pl.edu.agh.ki.grieg.decoder.spi;

import java.io.FileInputStream;
import java.io.IOException;

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
     * stream. 
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
    SourceDetails getDetails(FileInputStream stream) throws DecodeException,
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
    AudioFile open(FileInputStream stream) throws DecodeException, IOException;

}
