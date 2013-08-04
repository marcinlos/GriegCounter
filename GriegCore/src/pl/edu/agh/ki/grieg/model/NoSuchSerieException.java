package pl.edu.agh.ki.grieg.model;

import java.util.NoSuchElementException;

/**
 * Thrown when an operation is requested on the serie that does not exist in the
 * specified chart.
 * 
 * @author los
 */
public class NoSuchSerieException extends NoSuchElementException {

    public NoSuchSerieException() {
        // empty
    }

    public NoSuchSerieException(String s) {
        super(s);
    }

    public static NoSuchSerieException named(String serieName) {
        return new NoSuchSerieException("No serie \"" + serieName + "\"");
    }

}
