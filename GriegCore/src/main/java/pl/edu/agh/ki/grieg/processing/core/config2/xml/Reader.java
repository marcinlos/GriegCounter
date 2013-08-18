package pl.edu.agh.ki.grieg.processing.core.config2.xml;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.Context;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

public interface Reader<T> {
    
    T read(Element node, Context ctx) throws ConfigException;

}
