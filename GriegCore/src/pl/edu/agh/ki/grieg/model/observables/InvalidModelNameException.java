package pl.edu.agh.ki.grieg.model.observables;

/**
 * Thrown when an invalid model name is encountered.
 * 
 * @author los
 */
public class InvalidModelNameException extends ModelException {

    public InvalidModelNameException() {
        // empty
    }

    public InvalidModelNameException(String message) {
        super(message);
    }

    public InvalidModelNameException(Throwable cause) {
        super(cause);
    }

    public InvalidModelNameException(String message, Throwable cause) {
        super(message, cause);
    }

}
