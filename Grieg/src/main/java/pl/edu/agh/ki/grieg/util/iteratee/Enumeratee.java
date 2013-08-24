package pl.edu.agh.ki.grieg.util.iteratee;

/**
 * Enumerator transforming stream of {@code T} items into the stream of
 * {@code S} items.
 * 
 * <p>
 * Enumeratee is an abstraction of stream transformer (decoder) - takes values
 * from some {@linkplain Enumerator}, and produces a stream of output values,
 * thus being itself an {@linkplain Enumerator}.
 * 
 * @author los
 * 
 * @param <S>
 *            Type of consumed items (input stream)
 * @param <T>
 *            Type of produced items (output stream)
 */
public interface Enumeratee<S, T> extends Iteratee<S>, Enumerator<T> {

}
