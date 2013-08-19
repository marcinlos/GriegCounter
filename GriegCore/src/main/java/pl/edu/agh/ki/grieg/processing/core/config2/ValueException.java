package pl.edu.agh.ki.grieg.processing.core.config2;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;

/**
 * Base class for exceptions denoting errors occuring during value nodes
 * evaluation.
 * 
 * @author los
 */
public class ValueException extends ConfigException {

    public ValueException(String message) {
        super(message);
    }

    public ValueException(Throwable cause) {
        super(cause);
    }

    public ValueException(String message, Throwable cause) {
        super(message, cause);
    }

}
