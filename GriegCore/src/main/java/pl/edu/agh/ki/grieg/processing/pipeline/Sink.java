package pl.edu.agh.ki.grieg.processing.pipeline;

import pl.edu.agh.ki.grieg.util.iteratee.Iteratee;

interface Sink<T> extends Node {

    Class<T> getInputType();

    Iteratee<T> getSink();
    
}
