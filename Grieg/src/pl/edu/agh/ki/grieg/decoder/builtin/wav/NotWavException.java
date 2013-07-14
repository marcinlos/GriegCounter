package pl.edu.agh.ki.grieg.decoder.builtin.wav;

import pl.edu.agh.ki.grieg.decoder.InvalidFormatException;

/**
 * Thrown when RIFF file which does not contain WAV data is encountered.
 * 
 * @author los
 */
public class NotWavException extends InvalidFormatException {

    public NotWavException() {
        // empty
    }

    public NotWavException(String message) {
        super(message);
    }

    public NotWavException(Throwable cause) {
        super(cause);
    }

    public NotWavException(String message, Throwable cause) {
        super(message, cause);
    }

}
