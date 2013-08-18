package pl.edu.agh.ki.grieg.processing.core.config2;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;

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
