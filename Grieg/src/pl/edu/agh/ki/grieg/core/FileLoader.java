package pl.edu.agh.ki.grieg.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ServiceLoader;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.decoder.DecoderManager;
import pl.edu.agh.ki.grieg.decoder.NoSuitableDecoderException;
import pl.edu.agh.ki.grieg.decoder.builtin.mp3.Mp3Parser;
import pl.edu.agh.ki.grieg.decoder.builtin.wav.WavFileParser;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;
import pl.edu.agh.ki.grieg.io.AudioFile;

import com.google.common.io.Closeables;
import com.google.common.io.Files;

/**
 * 
 * 
 * @author los
 */
public class FileLoader {

    private static final Logger logger = LoggerFactory
            .getLogger(FileLoader.class);

    /** Root decoder manager, ancestor of all the managers */
    private static final DecoderManager root;

    /** Collection of audio parsers */
    private final DecoderManager decoders = new DecoderManager(root);

    static {
        logger.info("Initializing file loader system");
        root = new DecoderManager();

        // register builtin decoders here
        root.register(new WavFileParser());
        root.register(new Mp3Parser());

        // gather all the implementations
        for (AudioFormatParser p : ServiceLoader.load(AudioFormatParser.class)) {
            root.register(p);
        }
        logDetails();
        logger.info("File loader system initialization completed");
    }

    private static void logDetails() {
        Set<String> extensions = root.getKnownExtensions();
        int parserCount = root.getAllDecoders().size();
        int extCount = extensions.size();
        logger.debug("Found {} parsers for {} formats", parserCount, extCount);
        if (logger.isTraceEnabled()) {
            logger.trace("Parsers:");
            for (String ext : extensions) {
                logger.trace("{} -> {}", ext, root.getByExtension(ext));
            }
        }
    }

    /**
     * Creates an audio file object.
     * 
     * @param file
     *            File containing audio data to load
     * @return {@code AudioFile}
     * @throws NoSuitableDecoderException
     *             If no decoder capable of decoding this file has been found
     * @throws NoSuitableDecoderException
     *             If there is no parser capable of reading the file
     * @throws IOException
     *             If plain IO error occured
     */
    public AudioFile loadFile(File file) throws NoSuitableDecoderException,
            IOException {
        AudioFormatParser parser = findParser(file);
        return new AudioFile(file, parser);
    }

    /**
     * Searches for a parser capable of reading audio data from the given file.
     * 
     * @param file
     *            File to find parser fo
     * @return Parser for the file
     * @throws NoSuitableDecoderException
     *             If there is no parser capable of reading the file
     * @throws IOException
     *             If an IO error occurs
     */
    public AudioFormatParser findParser(File file)
            throws NoSuitableDecoderException, IOException {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            final FileChannel channel = stream.getChannel();
            for (AudioFormatParser parser : decoders.getByExtension(file)) {
                if (parser.readable(stream)) {
                    return parser;
                } else {
                    // rewind
                    channel.position(0);
                }
            }
            String extension = Files.getFileExtension(file.getName());
            throw new NoSuitableDecoderException(extension);
        } finally {
            Closeables.close(stream, true);
        }
    }

    /**
     * @return Decoder manager used by this file loader
     */
    public DecoderManager getDecoderManager() {
        return decoders;
    }

    /**
     * @return Collection of common extensions of files usually parsable by this
     *         {@link FileLoader}. Does not need to be accurate.
     */
    public Set<String> getKnownExtensions() {
        return decoders.getKnownExtensions();
    }

}
