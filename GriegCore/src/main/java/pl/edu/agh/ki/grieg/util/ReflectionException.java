package pl.edu.agh.ki.grieg.util;

public class ReflectionException extends Exception {

    public ReflectionException() {
        // empty
    }

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
