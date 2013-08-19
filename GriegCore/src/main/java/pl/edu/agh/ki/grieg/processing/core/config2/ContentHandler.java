package pl.edu.agh.ki.grieg.processing.core.config2;

public interface ContentHandler<T> {

    Object evaluate(T content);
    
}
