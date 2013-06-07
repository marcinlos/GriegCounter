package pl.edu.agh.ki.grieg.utils;

/**
 * Static helper class containing iteratee combinators and other convenience
 * methods.
 * 
 * @author los
 */
public class Iteratees {

    private Iteratees() {
        // non-instantiable
    }

    /**
     * Produces a new {@code Enumeratee} consuming {@code S} stream and
     * producing {@code U} stream by composing two {@linkplain Enumeratee}s,
     * i.e. connecting {@code inner}'s output and {@code outer}'s input.
     * 
     * @param outer
     *            Outer {@code Enumeratee}, final producer
     * @param inner
     *            Inner {@code Enumeratee}, initial consumer
     * @return {@code Enumeratee} combining the {@code inner} and {@code outer}
     */
    public static <S, T, U> Enumeratee<S, U> compose(Enumeratee<T, U> outer,
            Enumeratee<S, T> inner) {
        return new CompositeEnumeratee<S, T, U>(outer, inner);
    }

    /**
     * Produces new {@code Enumeratee} consuming {@code S} stream, passing the
     * values to the {@code transform} and pushing the results to the output
     * stream.
     * 
     * @param transform
     *            Function used to transform stream chunks
     * @return {@code Enumeratee} transforming the input stream
     */
    public static <S, T> Enumeratee<S, T> mapper(Function<S, T> transform) {
        return new SimpleEnumeratee<S, T>(transform);
    }

}
