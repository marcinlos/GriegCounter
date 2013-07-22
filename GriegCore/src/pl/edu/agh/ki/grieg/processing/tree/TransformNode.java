package pl.edu.agh.ki.grieg.processing.tree;

import pl.edu.agh.ki.grieg.utils.iteratee.Enumeratee;
import pl.edu.agh.ki.grieg.utils.iteratee.Enumerator;
import pl.edu.agh.ki.grieg.utils.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.utils.iteratee.Iteratees;

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
