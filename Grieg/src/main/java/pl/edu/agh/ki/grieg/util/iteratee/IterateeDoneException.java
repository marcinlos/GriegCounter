package pl.edu.agh.ki.grieg.util.iteratee;

/**
 * Exception thrown when iteratee's {@linkplain Iteratee#step} method is called
 * after the iteratee has finished processing input and returned
 * {@linkplain State#Done} from some previous call.
 * 
 * @author los
 */
public class IterateeDoneException extends IllegalStateException {

    // no need for fancy constructors

}
