package pl.edu.agh.ki.grieg.decoder.builtin.mp3;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Decoder;
import pl.edu.agh.ki.grieg.data.SourceDetails;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.AudioStream;

public class Mp3Parser implements AudioFormatParser {

    private static final Iterable<String> EXTS = Arrays.asList("mp3");

    static final Decoder decoder = new Decoder();

    @Override
    public Iterable<String> extensions() {
        return EXTS;
    }

    public SourceDetails getDetails(Bitstream stream) throws DecodeException,
            IOException {
        try {
            return new Mp3Stream(stream).getDetails();
        } finally {
            stream.closeFrame();
        }
    }

    @Override
    public AudioFile open(InputStream stream) throws DecodeException,
            IOException {
        Bitstream input = new Bitstream(stream);
        SourceDetails details = getDetails(input);
        Mp3Stream audioStream = new Mp3Stream(input);
        return new AudioFile(details, audioStream);
    }

    @Override
    public AudioStream openStream(InputStream stream) throws DecodeException,
            IOException {
        Bitstream input = new Bitstream(stream);
        return new Mp3Stream(input);
    }

    @Override
    public boolean readable(InputStream stream) throws IOException {
        try {
            getDetails(new Bitstream(stream));
            return true;
        } catch (DecodeException e) {
            return false;
        }
    }

}
