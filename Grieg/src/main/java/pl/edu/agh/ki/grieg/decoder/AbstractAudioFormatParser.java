package pl.edu.agh.ki.grieg.decoder;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;

/**
 * Abstract base class for {@linkplain AudioFormatParser}s. Implements
 * {@link #readable(InputStream)} in terms of {@link #openStream(InputStream)},
 * exploiting the fact that usually if the latter does not throw a
 * {@linkplain DecodeException}, it is capable of decoding the input.
 * 
 * @author los
 */
public abstract class AbstractAudioFormatParser implements AudioFormatParser {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * {@inheritDoc}
     * 
     * <p>
     * If {@link #openStream(InputStream)} does not throw, it returns
     * {@code true}.
     */
    @Override
    public boolean readable(InputStream stream) throws IOException {
        try {
            openStream(stream);
            return true;
        } catch (DecodeException e) {
            logger.debug("File not readable", e);
            return false;
        }
    }

}
