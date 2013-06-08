package pl.edu.agh.ki.grieg.utils.iteratee;

/**
 * Automatic enumerator, producing the whole stream in one method.
 * 
 * <p>
 * This enumerator represents fully autonomous enumerator, which produces the
 * whole stream and pushing all its chunks inside the {@link #run()} method.
 * Example: enumerator wrapping collection.
 * 
 * <p>
 * No additional methods extending
 * combined {@code Enumerator} and {@code Runnable} interface are defined here,
 * the concept is named for convenience.
 * 
 * @author los
 * 
 * @param <T>
 *            Type of produced elements
 */
public interface AutoEnumerator<T> extends Enumerator<T>, Runnable {

}
