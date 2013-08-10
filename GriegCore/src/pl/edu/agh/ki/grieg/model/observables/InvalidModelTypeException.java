package pl.edu.agh.ki.grieg.model.observables;

/**
 * Thrown upon a type mismatch during some model operation that checks it, e.g.
 * when registering the listener.
 * 
 * @author los
 */
public class InvalidModelTypeException extends ModelException {

    public InvalidModelTypeException() {
        // empty
    }

    public InvalidModelTypeException(String message) {
        super(message);
    }

    public InvalidModelTypeException(Throwable cause) {
        super(cause);
    }

    public InvalidModelTypeException(String message, Throwable cause) {
        super(message, cause);
    }

}
