package pl.edu.agh.ki.grieg.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;
import pl.edu.agh.ki.grieg.util.Key;
import pl.edu.agh.ki.grieg.util.Keys;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.PropertyMap;

import com.google.common.base.Objects;

/**
 * Audio file, consisting of a {@link File} object and a parser capable of
 * interpreting the data it contains. Simple representation of an audio file.
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

    /** Metadata retrieved so far */
    private final Properties infoCache;

    /**
     * Creates new {@code AudioFile} using given path and parser.
     */
    public AudioFile(File file, AudioFormatParser parser) {
        this.file = file;
        this.parser = parser;
        this.infoCache = new PropertyMap();
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
     * size
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
     * Creates a new audio source from contained file using default buffer size
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
     * @return Cached metainfo
     */
    public Properties getInfo() {
        return infoCache;
    }

    /**
     * Retrieves data if it has been in the cache already. That is, this method
     * does not try to extract required information from the audio file.
     * 
     * @param key
     *            Information to load
     * @return Requested information of {@code null} if it is not available
     */
    public <T> T get(Key<T> key) {
        return infoCache.get(key);
    }

    /**
     * Attempts to determine, exmining the file if necessary, and returns,
     * specified metainfo about the audio file represented by this object.
     * Updates metainfo cache.
     * 
     * @param key
     *            Information to be provided
     * @return Value associated with the {@code key}
     * @throws DecodeException
     *             If a decoding fails
     * @throws IOException
     *             If an IO error occurs
     */
    public <T> T determine(Key<T> key) throws DecodeException, IOException {
        parser.getDetails(file, Keys.set(key), infoCache);
        return infoCache.get(key);
    }

    /**
     * Updates metainfo cache with all the information described by the
     * {@code desired} (if it manages to retrieve it) and returns this cache.
     * Examines the file if necessary.
     * 
     * @param desired
     *            Set of needed pieces of information
     * @param config
     *            Additional configuration
     * @return Metainfo cache
     * @throws DecodeException
     *             If a decoding fails
     * @throws IOException
     *             If an IO error occurs
     */
    public Properties computeAll(Set<Key<?>> desired) throws DecodeException,
            IOException {
        parser.getDetails(file, desired, infoCache);
        return infoCache;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
            .add("file", file)
            .add("parser", parser)
            .toString();
    }

}
