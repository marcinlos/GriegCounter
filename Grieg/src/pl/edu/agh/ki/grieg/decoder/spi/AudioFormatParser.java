package pl.edu.agh.ki.grieg.decoder.spi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.meta.MetaInfo;
import pl.edu.agh.ki.grieg.meta.MetaKey;

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

    /**
     * Retrieves details about the specified audio file. Desired details are
     * specified as the argument, so that the method can avoid expensive
     * computation (e.g. calculating total number of samples in the file).
     * Implementation is not obliged to provide all the desired details, nor
     * constrained to provide only desired ones.
     * 
     * @param file
     *            File to investigate
     * @param desired
     *            Set of metadata entries that are desired by the caller
     * @param info
     *            Container to insert data into
     * @return Details about the audio file
     * @throws IOException
     *             If an IO error occurs
     * @throws DecodeException
     *             If the file could not be properly decoded
     */
    void getDetails(File file, Set<MetaKey<?>> desired, MetaInfo info)
            throws IOException, DecodeException;

}
