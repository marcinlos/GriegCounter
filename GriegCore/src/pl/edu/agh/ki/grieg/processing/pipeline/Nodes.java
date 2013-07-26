package pl.edu.agh.ki.grieg.processing.pipeline;

import pl.edu.agh.ki.grieg.utils.iteratee.Enumeratee;
import pl.edu.agh.ki.grieg.utils.iteratee.Enumerator;
import pl.edu.agh.ki.grieg.utils.iteratee.Iteratee;

final class Nodes {

    private Nodes() {
        // non-instantiable
    }

    public static <T> Sink<T> make(Iteratee<? super T> sink, Class<T> input) {
        return new SinkNode<T>(sink, input);
    }

    public static <T> Source<T> make(Enumerator<? extends T> source,
            Class<T> output) {
        return new SourceNode<T>(source, output);
    }

    public static <S, T> Transform<S, T> make(
            Enumeratee<? super S, ? extends T> transform, Class<S> input,
            Class<T> output) {
        return new TransformNode<S, T>(transform, input, output);
    }

}
