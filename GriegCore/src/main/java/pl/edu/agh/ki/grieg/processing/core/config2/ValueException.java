package pl.edu.agh.ki.grieg.processing.core.config2;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;

/**
 * Base class for exceptions denoting errors occuring during value nodes
 * evaluation.
 * 
 * @author los
 */
public class ValueException extends ConfigException {

    /**
     * Creates {@link ValueException} with specified error message.
     * 
     * @param message
     *            Error description
     */
    public ValueException(String message) {
        super(message);
    }

    /**
     * Creates {@link ValueException} with specified cause.
     * 
     * @param cause
     *            {@link Throwable} that caused this excaption
     */
    public ValueException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates {@link ValueException} with specified error message and cause.
     * 
     * @param message
     *            Error description
     * @param cause
     *            {@link Throwable} that caused this excaption
     */
    public ValueException(String message, Throwable cause) {
        super(message, cause);
    }

}
