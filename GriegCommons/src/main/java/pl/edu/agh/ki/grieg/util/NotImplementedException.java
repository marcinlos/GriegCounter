package pl.edu.agh.ki.grieg.util;

/**
 * Exception thrown to fill implementation holes. Apparently there is no exact
 * equivalent in JDK.
 *
 * @author los
 */
public class NotImplementedException extends RuntimeException {

    public NotImplementedException() {
        // empty
    }

    public NotImplementedException(String message) {
        super(message);
    }

}
