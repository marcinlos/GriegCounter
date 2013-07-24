package pl.edu.agh.ki.grieg.decoder.builtin.wav;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;

import pl.edu.agh.ki.grieg.decoder.AbstractAudioFormatParser;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.meta.AudioKeys;
import pl.edu.agh.ki.grieg.utils.Key;
import pl.edu.agh.ki.grieg.utils.Properties;

import com.google.common.io.Closeables;

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
    public WavStream openStream(InputStream stream) throws DecodeException,
            IOException {
        return new WavStream(stream);
    }

    private AudioDetails getDetails(File file) throws IOException,
            DecodeException {
        InputStream stream = null;
        try {
            stream = new BufferedInputStream(new FileInputStream(file));
            WavStream audio = openStream(stream);
            return audio.getDetails();
        } finally {
            Closeables.close(stream, true);
        }
    }

    @Override
    public void getDetails(File file, Set<Key<?>> desired, Properties config,
            Properties info) throws IOException, DecodeException {
        AudioDetails details = getDetails(file);
        info.put(AudioKeys.SAMPLES, details.getSampleCount());
        info.put(AudioKeys.DURATION, details.getLength());
        info.put(AudioKeys.FORMAT, details.getFormat());
    }

}
