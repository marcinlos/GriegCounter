package pl.edu.agh.ki.grieg.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ServiceLoader;

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

    /** Root decoder manager, ancestor of all the managers */
    private static final DecoderManager root = new DecoderManager();

    /** Collection of audio parsers */
    private DecoderManager decoders = new DecoderManager(root);

    static {
        // register builtin decoders here
        root.register(new WavFileParser());
        root.register(new Mp3Parser());

        // gather all the implementations
        for (AudioFormatParser p : ServiceLoader.load(AudioFormatParser.class)) {
            root.register(p);
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

}
