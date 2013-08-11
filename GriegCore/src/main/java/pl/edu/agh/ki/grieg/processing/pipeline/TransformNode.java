package pl.edu.agh.ki.grieg.processing.pipeline;

import pl.edu.agh.ki.grieg.util.iteratee.Enumeratee;
import pl.edu.agh.ki.grieg.util.iteratee.Enumerator;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratees;

public class TransformNode<S, T> implements Transform<S, T> {

    public final Enumeratee<S, T> transform;

    public final Class<S> input;

    public final Class<T> output;

    public TransformNode(Enumeratee<? super S, ? extends T> transform,
            Class<S> input, Class<T> output) {
        this.transform = Iteratees.upcast(transform);
        this.input = input;
        this.output = output;
    }

    @Override
    public Class<T> getOutputType() {
        return output;
    }

    @Override
    public Class<S> getInputType() {
        return input;
    }

    @Override
    public Enumerator<T> getSource() {
        return getTransform();
    }

    @Override
    public Iteratee<S> getSink() {
        return getTransform();
    }

    @Override
    public Enumeratee<S, T> getTransform() {
        return transform;
    }

}
