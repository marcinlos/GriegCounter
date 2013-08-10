package pl.edu.agh.ki.grieg.model.observables;

/**
 * Thrown to indicate malformed submodel path.
 * 
 * @author los
 */
public class InvalidPathFormatException extends ModelException {

    public InvalidPathFormatException() {
        // empty
    }

    public InvalidPathFormatException(String message) {
        super(message);
    }

    public InvalidPathFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidPathFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}
