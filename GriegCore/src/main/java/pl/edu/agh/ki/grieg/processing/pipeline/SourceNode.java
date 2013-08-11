package pl.edu.agh.ki.grieg.processing.pipeline;

import pl.edu.agh.ki.grieg.util.iteratee.Enumerator;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratees;

class SourceNode<T> implements Source<T> {

    public final Enumerator<T> source;

    public final Class<T> output;

    public SourceNode(Enumerator<? extends T> source, Class<T> output) {
        this.source = Iteratees.upcast(source);
        this.output = output;
    }

    @Override
    public Class<T> getOutputType() {
        return output;
    }

    @Override
    public Enumerator<T> getSource() {
        return source;
    }

}
