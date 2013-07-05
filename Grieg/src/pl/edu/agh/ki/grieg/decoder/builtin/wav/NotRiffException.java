package pl.edu.agh.ki.grieg.decoder.builtin.wav;

import pl.edu.agh.ki.grieg.decoder.InvalidFormatException;

/**
 * Thrown when a file which is not a valid RIFF file is encountered.
 * 
 * @author los
 *
 */
public class NotRiffException extends InvalidFormatException {

    public NotRiffException() {
        // empty
    }

    public NotRiffException(String message) {
        super(message);
    }

    public NotRiffException(Throwable cause) {
        super(cause);
    }

    public NotRiffException(String message, Throwable cause) {
        super(message, cause);
    }

}
