package pl.edu.agh.ki.grieg.model.model;

/**
 * Thrown to indicate invalid operations attempted at the model.
 * 
 * @author los
 */
public class ModelException extends RuntimeException {

    public ModelException() {
        // empty
    }

    public ModelException(String message) {
        super(message);
    }

    public ModelException(Throwable cause) {
        super(cause);
    }

    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }

}
