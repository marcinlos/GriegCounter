package pl.edu.agh.ki.grieg.decoder.wav;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.decoder.AbstractAudioFormatParser;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.util.JAudioTaggerMetaExtractor;
import pl.edu.agh.ki.grieg.features.AudioFeatures;
import pl.edu.agh.ki.grieg.features.ExtractionContext;

import com.google.common.io.Closeables;

/**
 * Parser of WAV files contained in RIFF file format.
 * 
 * @author los
 */
public class WavFileParser extends AbstractAudioFormatParser {

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
    public void extractFeatures(File file, ExtractionContext context)
            throws IOException, DecodeException {
        JAudioTaggerMetaExtractor.process(file, context);
        AudioDetails details = getDetails(file);
        context.setFeature(AudioFeatures.SAMPLES, details.getSampleCount());
        context.setFeature(AudioFeatures.DURATION, details.getLength());
        context.setFeature(AudioFeatures.FORMAT, details.getFormat());
    }

}
