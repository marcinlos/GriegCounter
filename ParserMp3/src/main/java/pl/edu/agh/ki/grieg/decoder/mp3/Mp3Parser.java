package pl.edu.agh.ki.grieg.decoder.mp3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javazoom.jl.decoder.Bitstream;
import pl.edu.agh.ki.grieg.decoder.AbstractAudioFormatParser;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.io.AudioStream;

public class Mp3Parser extends AbstractAudioFormatParser {

    private static final Iterable<String> EXTS = Arrays.asList("mp3");

    @Override
    public Iterable<String> extensions() {
        return EXTS;
    }

    @Override
    public AudioStream openStream(InputStream stream) throws DecodeException,
            IOException {
        Bitstream input = new Bitstream(stream);
        return new Mp3Stream(input);
    }

    @Override
    public void extractFeatures(File file, ExtractionContext context) throws IOException,
            DecodeException {
        MetaExtractor extractor = new MetaExtractor(file, context);
        extractor.extract();

    }

}
