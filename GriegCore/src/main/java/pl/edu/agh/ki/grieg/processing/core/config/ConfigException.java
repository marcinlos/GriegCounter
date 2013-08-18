package pl.edu.agh.ki.grieg.processing.core.config;

/**
 * Base class for exceptions indicating problem regarding configuration.
 * 
 * @author los
 */
public class ConfigException extends Exception {

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
