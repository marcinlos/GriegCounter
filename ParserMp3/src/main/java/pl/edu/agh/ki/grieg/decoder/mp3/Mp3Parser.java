package pl.edu.agh.ki.grieg.decoder.mp3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javazoom.jl.decoder.Bitstream;
import pl.edu.agh.ki.grieg.decoder.AbstractAudioFormatParser;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.io.AudioStream;

public class Mp3Parser extends AbstractAudioFormatParser {

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
