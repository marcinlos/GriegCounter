package pl.edu.agh.ki.grieg.decoder.builtin.wav;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.spi.AbstractAudioFormatParser;
import pl.edu.agh.ki.grieg.io.AudioStream;

/**
 * Parser of WAV files contained in RIFF file format.
 * 
 * @author los
 */
public class WavFileParser extends AbstractAudioFormatParser {

    private static final Iterable<String> EXTS = Arrays.asList("wav");

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<String> extensions() {
        return EXTS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AudioStream openStream(InputStream stream) throws DecodeException,
            IOException {
        return new WavStream(stream);
    }

}
