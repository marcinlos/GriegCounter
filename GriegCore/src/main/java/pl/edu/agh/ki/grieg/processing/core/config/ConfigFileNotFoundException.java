package pl.edu.agh.ki.grieg.processing.core.config;

/**
 * Exception thrown when the configuration file is missing and cannot be read.
 * 
 * @author los
 */
public class ConfigFileNotFoundException extends ResourceNotFoundException {

    public ConfigFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigFileNotFoundException(String message) {
        super(message);
    }

    public ConfigFileNotFoundException(Throwable cause) {
        super(cause);
    }

}
