package pl.edu.agh.ki.grieg.processing.pipeline;

import pl.edu.agh.ki.grieg.utils.iteratee.Enumerator;

interface Source<T> extends Node {
    
    Class<T> getOutputType();

    Enumerator<T> getSource();
    
}
