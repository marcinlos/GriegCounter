package pl.edu.agh.ki.grieg.decoder;

import pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser;

/**
 * Thrown upon an attempt to parse file format not supported by the used
 * {@linkplain AudioFormatParser}.
 * 
 * <p>
 * Note: it's not really a {@linkplain InvalidFormatException}, since the input stream
 * may still be perfectly valid, just not the right format.
 * 
 * @author los
 */
public class UnsupportedFormatException extends DecodeException {

    public UnsupportedFormatException() {
        // empty
    }

    public UnsupportedFormatException(String message) {
        super(message);
    }

    public UnsupportedFormatException(Throwable cause) {
        super(cause);
    }

    public UnsupportedFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}
