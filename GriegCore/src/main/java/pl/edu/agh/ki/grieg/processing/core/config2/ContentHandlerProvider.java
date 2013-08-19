package pl.edu.agh.ki.grieg.processing.core.config2;

public interface ContentHandlerProvider {
    
    ContentHandler<?> forQualifier(String qualifier);

}
