package pl.edu.agh.ki.grieg.util.parsing;

/**
 * Thrown in case an error occured during creating a parameter parser.
 * 
 * @author los
 */
public class ParserFactoryException extends Exception {

    public ParserFactoryException() {
    }

    public ParserFactoryException(String message) {
        super(message);
    }

    public ParserFactoryException(Throwable cause) {
        super(cause);
    }

    public ParserFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

}
