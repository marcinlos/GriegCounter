package pl.edu.agh.ki.grieg.decoder.builtin.wav;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import pl.edu.agh.ki.grieg.data.SourceDetails;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.AudioStream;

public class WavFileParser implements AudioFormatParser {

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
    public AudioFile open(InputStream stream) throws DecodeException,
            IOException {
        WavStream audioStream = new WavStream(stream);
        SourceDetails details = audioStream.getDetails();
        return new AudioFile(details, audioStream);
    }

    @Override
    public AudioStream openStream(InputStream stream) throws DecodeException,
            IOException {
        return new WavStream(stream);
    }

    @Override
    public boolean readable(InputStream stream) throws IOException {
        try {
            openStream(stream);
            return true;
        } catch (DecodeException e) {
            return false;
        }
    }

}
