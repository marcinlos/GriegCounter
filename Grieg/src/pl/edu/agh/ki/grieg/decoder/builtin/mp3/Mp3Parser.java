package pl.edu.agh.ki.grieg.decoder.builtin.mp3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Set;

import javazoom.jl.decoder.Bitstream;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.spi.AbstractAudioFormatParser;
import pl.edu.agh.ki.grieg.io.AudioStream;
import pl.edu.agh.ki.grieg.meta.MetaInfo;
import pl.edu.agh.ki.grieg.meta.MetaKey;

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
    public MetaInfo getDetails(File file, Set<MetaKey<?>> desired)
            throws IOException, DecodeException {
        return new MetaInfo();
    }

}
