package pl.edu.agh.ki.grieg.model;

/**
 * Thrown upon an attempt to register listener for nonexistant model.
 * 
 * @author los
 */
public class NoSuchModelException extends ModelException {

    public NoSuchModelException() {
        // empty
    }

    public NoSuchModelException(String message) {
        super(message);
    }

    public NoSuchModelException(Throwable cause) {
        super(cause);
    }

    public NoSuchModelException(String message, Throwable cause) {
        super(message, cause);
    }

}
