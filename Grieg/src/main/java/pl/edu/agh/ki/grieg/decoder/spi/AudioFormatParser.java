package pl.edu.agh.ki.grieg.decoder.spi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.features.FeaturesListener;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.util.ProgressListener;

/**
 * Provider interface of audio file format(s) parser.
 * 
 * @author los
 */
public interface AudioFormatParser {

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
     * Retrieves details about the specified file. Features to extract,
     * precomputed features and specific configuration settings are taken from
     * the passed context. Implementation if not obliged to provide all the
     * requested features, nor is it constrained to provide only these.
     * 
     * <p>
     * Two kinds of listeners are associated with the feature extraction
     * process:
     * <ul>
     * <li> {@link ProgressListener} for receiving notifications about the
     * "lifecycle" of the extraction
     * <li> {@link FeaturesListener} for receiving notifications about extracted
     * features during the extraction
     * </ul>
     * 
     * @param file
     *            File to be examined
     * 
     * @param context
     *            Structure describing the extraction process
     * @throws IOException
     *             If an IO error occurs
     * @throws DecodeException
     *             If the file could not be properly decoded
     */
    void extractFeatures(File file, ExtractionContext context)
            throws IOException, DecodeException;

}
