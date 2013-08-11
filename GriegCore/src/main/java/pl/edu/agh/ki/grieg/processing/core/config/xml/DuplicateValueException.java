package pl.edu.agh.ki.grieg.processing.core.config.xml;

public class DuplicateValueException extends XmlConfigException {

    public DuplicateValueException() {
        // empty
    }

    public DuplicateValueException(String message) {
        super(message);
    }

    public DuplicateValueException(Throwable cause) {
        super(cause);
    }

    public DuplicateValueException(String message, Throwable cause) {
        super(message, cause);
    }

}
