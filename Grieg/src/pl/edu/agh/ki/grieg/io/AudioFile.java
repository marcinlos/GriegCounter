package pl.edu.agh.ki.grieg.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;
import pl.edu.agh.ki.grieg.meta.ExtractionContext;

import com.google.common.base.Objects;

/**
 * Audio file, consisting of a {@link File} object representing filesystem path
 * of the file and a parser capable of interpreting the data it contains.
 * 
 * @author los
 */
public class AudioFile {

    /**
     * Default buffer size for sources returned by parameterless
     * {@link #openSource()} invocations
     */
    public static final int DEFAUL_BUFFER_SIZE = 2048;

    /** File containing audio data */
    private final File file;

    /** Parser suitable for dealing with the format of the file */
    private final AudioFormatParser parser;

    /**
     * Creates new {@code AudioFile} using given path and parser.
     */
    public AudioFile(File file, AudioFormatParser parser) {
        this.file = file;
        this.parser = parser;
    }

    /**
     * @return File containing audio data
     */
    public File getFile() {
        return file;
    }

    /**
     * @return Parser compatible with the audio format of the file
     */
    public AudioFormatParser getParser() {
        return parser;
    }

    /**
     * Creates new {@link AudioStream}.
     * 
     * @return {@code AudioStream} reading and decoding data from the file
     * @throws IOException
     *             If an IO error occurs
     * @throws DecodeException
     *             If the data contained in the file cannot be properly decoded
     */
    public AudioStream openStream() throws IOException, DecodeException {
        return parser.openStream(new FileInputStream(file));
    }

    /**
     * Creates a new audio source from contained file using specified buffer
     * size.
     * 
     * @param bufferSize
     *            Size of the source buffer
     * @return Audio source - {@link SampleEnumerator}
     * @throws IOException
     *             If an IO error occurs
     * @throws DecodeException
     *             If the data contained in the file cannot be properly decoded
     */
    public SampleEnumerator openSource(int bufferSize) throws IOException,
            DecodeException {
        return new StreamSampleEnumerator(openStream(), bufferSize);
    }

    /**
     * Creates a new audio source from contained file using default buffer size.
     * 
     * @return Audio source - {@link SampleEnumerator}
     * @throws IOException
     *             If an IO error occurs
     * @throws DecodeException
     *             If the data contained in the file cannot be properly decoded
     */
    public SampleEnumerator openSource() throws DecodeException, IOException {
        return openSource(DEFAUL_BUFFER_SIZE);
    }

    /**
     * Attempts to determine features of the audio file specified by the
     * {@code context}. Delegates this task to the audio parser suitable for
     * this file.
     * 
     * @param context
     *            Configuration of the extraction process
     * @throws DecodeException
     *             If the data contained in the file cannot be properly decoded
     * @throws IOException
     *             If an IO error occurs
     */
    public void extractFeatures(ExtractionContext context)
            throws DecodeException, IOException {
        parser.extractFeatures(file, context);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("file", file)
                .add("parser", parser).toString();
    }

}
