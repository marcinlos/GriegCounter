package pl.edu.agh.ki.grieg.processing.pipeline;

import pl.edu.agh.ki.grieg.util.iteratee.Enumeratee;

interface Transform<S, T> extends Source<T>, Sink<S> {
    
    Enumeratee<S, T> getTransform();
    
}
