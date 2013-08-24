package pl.edu.agh.ki.grieg.decoder;

/**
 * Base class for exceptions during decoding caused by malformed input.
 * 
 * @author los
 */
public class InvalidFormatException extends DecodeException {

    public InvalidFormatException() {
        // empty
    }

    public InvalidFormatException(String message) {
        super(message);
    }

    public InvalidFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}
