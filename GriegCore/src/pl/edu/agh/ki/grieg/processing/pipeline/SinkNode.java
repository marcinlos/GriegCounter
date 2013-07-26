package pl.edu.agh.ki.grieg.processing.pipeline;

import pl.edu.agh.ki.grieg.utils.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.utils.iteratee.Iteratees;

class SinkNode<T> implements Sink<T> {
    
    public final Iteratee<T> sink; 
    
    public final Class<T> input;

    public SinkNode(Iteratee<? super T> sink, Class<T> input) {
        this.sink = Iteratees.upcast(sink);
        this.input = input;
    }

    @Override
    public Class<T> getInputType() {
        return input;
    }

    @Override
    public Iteratee<T> getSink() {
        return sink;
    }

}
