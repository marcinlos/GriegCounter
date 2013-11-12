package pl.edu.agh.ki.grieg.decoder.ogg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.decoder.AbstractAudioFormatParser;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.io.AudioStream;

public class OggParser extends AbstractAudioFormatParser {

    @Override
    public AudioStream openStream(InputStream stream) throws DecodeException,
            IOException {
        throw new DecodeException("I cannot really decode yet");
    }

    @Override
    public void extractFeatures(File file, ExtractionContext context)
            throws IOException, DecodeException {
        // empty
    }

}
