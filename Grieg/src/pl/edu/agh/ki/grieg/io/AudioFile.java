package pl.edu.agh.ki.grieg.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;
import pl.edu.agh.ki.grieg.meta.Keys;
import pl.edu.agh.ki.grieg.meta.MetaInfo;
import pl.edu.agh.ki.grieg.meta.MetaKey;

import com.google.common.collect.Sets;

/**
 * Audio file, consisting of a {@link File} object and a parser capable of
 * interpreting the data it contains. Simple representation of an audio file.
 * 
 * @author los
 */
public class AudioFile {

    public static final int DEFAUL_BUFFER_SIZE = 2048;

    private MetaInfo infoCache;

    private final File file;
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

    public SampleEnumerator openSource(int bufferSize) throws IOException,
            DecodeException {
        return new StreamSampleEnumerator(openStream(), bufferSize);
    }

    public SampleEnumerator openSource() throws DecodeException, IOException {
        return openSource(DEFAUL_BUFFER_SIZE);
    }

    /**
     * @return Cached metainfo
     */
    public MetaInfo getInfo() {
        if (infoCache == null) {
            infoCache = new MetaInfo();
        }
        return infoCache;
    }

    /**
     * Attempts to retrieve, if necessary, and returns, specified metainfo about
     * the audio file represented by this object.
     * 
     * @param key
     * @return
     * @throws DecodeException
     * @throws IOException
     */
    public <T> T getInfo(MetaKey<T> key) throws DecodeException, IOException {
        // make sure it exists
        getInfo();
        if (infoCache.contains(key.name)) {
            return infoCache.get(key);
        } else {
            parser.getDetails(file, Keys.set(key), infoCache);
            return infoCache.get(key);
        }
    }

}
