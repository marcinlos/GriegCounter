package pl.edu.agh.ki.grieg.decoder.builtin.wav;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.spi.AbstractAudioFormatParser;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.meta.MetaInfo;
import pl.edu.agh.ki.grieg.meta.MetaKey;

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

    @Override
    public MetaInfo getDetails(File file, Set<MetaKey<?>> desired)
            throws IOException, DecodeException {
        return new MetaInfo();
    }

}
