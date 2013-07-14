package pl.edu.agh.ki.grieg.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ServiceLoader;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.DecoderManager;
import pl.edu.agh.ki.grieg.decoder.NoSuitableDecoderException;
import pl.edu.agh.ki.grieg.decoder.builtin.mp3.Mp3Parser;
import pl.edu.agh.ki.grieg.decoder.builtin.wav.WavFileParser;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.utils.FileUtils;
import pl.edu.agh.ki.grieg.utils.Streams;

/**
 * 
 * 
 * @author los
 */
public class FileLoader {

    private DecoderManager decoders = new DecoderManager();

    private static FileLoader instance = new FileLoader();

    private FileLoader() {
        registerBuiltins();

        // gather all the implementations
        for (AudioFormatParser p : ServiceLoader.load(AudioFormatParser.class)) {
            decoders.register(p);
        }
    }

    private void registerBuiltins() {
        // register builtin decoders here
        decoders.register(new WavFileParser());
        decoders.register(new Mp3Parser());
    }

    /**
     * @return Instance of {@code FileLoader}
     */
    public static FileLoader getInstance() {
        return instance;
    }

    /**
     * 
     * @param file
     *            File containing audio data to load
     * @return {@code AudioFile}
     * @throws NoSuitableDecoderException
     *             If no decoder capable of decoding this file has been found
     * @throws DecodeException
     *             If some error occured during decoding
     * @throws IOException
     *             If plain IO error occured
     */
    public AudioFile loadFile(File file) throws DecodeException, IOException {
        for (AudioFormatParser parser : decoders.getByExtension(file)) {
            try {
                InputStream stream = new FileInputStream(file);
                return parser.open(stream);
            } catch (DecodeException e) {
                // carry on
                e.printStackTrace(System.err);
            }
        }
        throw new NoSuitableDecoderException(FileUtils.getExtension(file));
    }

    public AudioFormatParser findParser(File file) throws IOException {
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
            return null;
        } catch (IOException e) {
            Streams.close(stream);
            throw e;
        }
    }
}
