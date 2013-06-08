package pl.edu.agh.ki.grieg.decoder;

/**
 * Base class for exceptions during decoding caused by malformed input.
 * 
 * @author los
 */
public class FormatException extends DecodeException {

    public FormatException() {
        // empty
    }

    public FormatException(String message) {
        super(message);
    }

    public FormatException(Throwable cause) {
        super(cause);
    }

    public FormatException(String message, Throwable cause) {
        super(message, cause);
    }

}
