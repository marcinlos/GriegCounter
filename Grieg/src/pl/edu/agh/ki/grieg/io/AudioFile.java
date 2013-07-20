package pl.edu.agh.ki.grieg.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;

import com.google.common.collect.Sets;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;
import pl.edu.agh.ki.grieg.meta.MetaInfo;
import pl.edu.agh.ki.grieg.meta.MetaKey;

/**
 * Audio file, consisting of a {@link File} object and a parser capable of
 * interpreting the data it contains. Simple representation of an audio file.
 * 
 * @author los
 */
public class AudioFile {

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

    public MetaInfo getInfo() {
        if (infoCache == null) {
            infoCache = new MetaInfo();
        }
        return infoCache;
    }

    public <T> T getInfo(MetaKey<T> key) throws DecodeException, IOException {
        // make sure it exists
        getInfo();
        if (infoCache.contains(key.name)) {
            return infoCache.get(key);
        } else {
            parser.getDetails(file, singleton(key), infoCache);
            return infoCache.get(key);
        }
    }

    private static <T> Set<MetaKey<?>> singleton(MetaKey<T> key) {
        return Sets.<MetaKey<?>>newHashSet(key);
    }

}
