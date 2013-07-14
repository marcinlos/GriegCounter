package pl.edu.agh.ki.grieg.decoder.spi;

import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.decoder.DecodeException;

public abstract class AbstractAudioFormatParser implements AudioFormatParser {

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
