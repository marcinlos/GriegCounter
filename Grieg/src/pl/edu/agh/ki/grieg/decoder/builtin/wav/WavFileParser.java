package pl.edu.agh.ki.grieg.decoder.builtin.wav;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import pl.edu.agh.ki.grieg.data.SourceDetails;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.spi.AudioFileParser;
import pl.edu.agh.ki.grieg.io.AudioFile;

public class WavFileParser implements AudioFileParser {

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
    public SourceDetails getDetails(InputStream stream) throws DecodeException,
            IOException {
        WavStream audioStream = null;
        try {
            audioStream = new WavStream(stream);
            return audioStream.getDetails();
        } finally {
            audioStream.close();
        }
    }

}
