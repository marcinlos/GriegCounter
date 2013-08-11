package pl.edu.agh.ki.grieg.processing.core.config;

public class ResourceNotFoundException extends ConfigException {

    public ResourceNotFoundException() {
        // empty
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
