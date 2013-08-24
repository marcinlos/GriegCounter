package pl.edu.agh.ki.grieg.util.iteratee;

/**
 * Mapping between values of type {@code S} and {@code T}.
 * 
 * @author los
 * 
 * @param <S>
 *            Function input type
 * @param <T>
 *            Function output type
 */
public interface Function<S, T> {

    /**
     * Applies the function to the input value {@code x} and returns produced
     * output.
     * 
     * @param x
     *            Input value to the function
     * @return Output of the function applied to the input
     */
    T apply(S x);

}
