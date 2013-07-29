package pl.edu.agh.ki.grieg.processing.core.config;

public interface Property<T> {
    
    String getName();
    
    String getString();
    
    Class<? extends T> getType() throws ConfigException;
    
    T convert() throws ConfigException;

}
