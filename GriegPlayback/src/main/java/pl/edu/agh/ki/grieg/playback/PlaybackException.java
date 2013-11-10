package pl.edu.agh.ki.grieg.playback;

import pl.edu.agh.ki.grieg.io.AudioException;

/**
 * Thrown upon problems with audio playback.
 * 
 * @author los
 */
public class PlaybackException extends AudioException {

    public PlaybackException() {
        // empty
    }

    public PlaybackException(String message) {
        super(message);
    }

    public PlaybackException(Throwable cause) {
        super(cause);
    }

    public PlaybackException(String message, Throwable cause) {
        super(message, cause);
    }

}
