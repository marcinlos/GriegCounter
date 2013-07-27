package pl.edu.agh.ki.grieg.util.iteratee;

/**
 * Iteratee's state, returned by the {@linkplain Iteratee#step} method.
 * 
 * @author los
 */
public enum State {

    /** Iteratee awaits more input */
    Cont,

    /** Iteratee will not accept any more input */
    Done

}
