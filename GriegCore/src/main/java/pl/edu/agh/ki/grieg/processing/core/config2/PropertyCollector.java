package pl.edu.agh.ki.grieg.processing.core.config2;

import java.util.Collections;
import java.util.Map;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PropertyNode;

import com.google.common.collect.Maps;


/**
 * Class whose sole purpose is to accumulate property definitions. Using
 * {@link Evaluator}, it computes properties from property nodes, and stores
 * these pairs.
 * 
 * @author los
 */
public class PropertyCollector {

    private final Evaluator evaluator;
    
    private final ErrorHandler errorHandler;
    
    private final Map<String, Object> properties = Maps.newHashMap();

    public PropertyCollector(Evaluator evaluator, ErrorHandler errorHandler) {
        this.evaluator = evaluator;
        this.errorHandler = errorHandler;
    }
    
    
    public void consume(PropertyNode node) {
        String name = node.getName();
        try {
            Object value = evaluator.evaluate(node.getValue());
            if (! properties.containsKey(name)) {
                properties.put(name, value);
            } else {
                throw new PropertyRedefinitionException(name);
            }
        } catch (ConfigException e) {
            errorHandler.error(e);
        }
    }
    
    public void consume(Iterable<PropertyNode> nodes) {
        for (PropertyNode node : nodes) {
            consume(node);
        }
    }
    
    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }
    
}
