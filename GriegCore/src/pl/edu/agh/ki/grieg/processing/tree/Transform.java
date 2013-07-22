package pl.edu.agh.ki.grieg.processing.tree;

import pl.edu.agh.ki.grieg.utils.iteratee.Enumeratee;

interface Transform<S, T> extends Source<T>, Sink<S> {
    
    Enumeratee<S, T> getTransform();
    
}
