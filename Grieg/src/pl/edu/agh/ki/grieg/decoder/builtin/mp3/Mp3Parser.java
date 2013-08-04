package pl.edu.agh.ki.grieg.decoder.builtin.mp3;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javazoom.jl.decoder.Bitstream;
import pl.edu.agh.ki.grieg.decoder.AbstractAudioFormatParser;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.meta.ExtractionContext;

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
    public void extractFeatures(ExtractionContext context) throws IOException,
            DecodeException {
        MetaExtractor extractor = new MetaExtractor(context);
        extractor.extract();

    }

}
