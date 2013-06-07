package pl.edu.agh.ki.grieg.io;

/**
 * Base class for exceptions indicating problems with audio input, be it plain
 * IO errors, codec errors or any other exceptional condition.
 * 
 * @author los
 */
public class AudioException extends Exception {

    public AudioException() {
        // empty
    }

    public AudioException(String message) {
        super(message);
    }

    public AudioException(Throwable cause) {
        super(cause);
    }

    public AudioException(String message, Throwable cause) {
        super(message, cause);
    }

}
