package pl.edu.agh.ki.grieg.util.classpath;

public class ClasspathException extends Exception {

    public ClasspathException() {
        // empty
    }

    public ClasspathException(String message) {
        super(message);
    }

    public ClasspathException(Throwable cause) {
        super(cause);
    }

    public ClasspathException(String message, Throwable cause) {
        super(message, cause);
    }

}
