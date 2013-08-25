package pl.edu.agh.ki.grieg.decoder.discovery;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;
import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.io.AudioStream;

/**
 * Empty {@link AudioFormatParser} implementation for testing loader mechanisms.
 * 
 * @author los
 */
public class DummyAudioBase implements AudioFormatParser {

    @Override
    public AudioStream openStream(InputStream stream) throws DecodeException,
            IOException {
        return null;
    }

    @Override
    public boolean readable(InputStream stream) throws IOException {
        return false;
    }

    @Override
    public void extractFeatures(File file, ExtractionContext context)
            throws IOException, DecodeException {
    }

}
