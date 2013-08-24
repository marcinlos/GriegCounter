package pl.edu.agh.ki.grieg.decoder;

import pl.edu.agh.ki.grieg.io.AudioException;

/**
 * Base class for all decoding-related exceptions, such as bad format, 
 * premature EOF etc. Also wraps IO errors that occured during decoding.
 * 
 * @author los 
 */
public class DecodeException extends AudioException {

    public DecodeException() {
        // empty
    }

    public DecodeException(String message) {
        super(message);
    }

    public DecodeException(Throwable cause) {
        super(cause);
    }

    public DecodeException(String message, Throwable cause) {
        super(message, cause);
    }

}
