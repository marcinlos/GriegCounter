package pl.edu.agh.ki.grieg.model;

/**
 * Thrown upon a type mismatch during some model operation that checks it, e.g.
 * when registering the listener.
 * 
 * @author los
 */
public class InvalidModelTypeException extends ModelException {
    
    private final Class<?> expected;
    private final Class<?> actual;

    public InvalidModelTypeException(Class<?> expected, Class<?> actual) {
        this.expected = expected;
        this.actual = actual;
    }

    @Override
    public String toString() {
        return String.format("Expected %s, but was %s", expected, actual);
    }

}
