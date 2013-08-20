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

    /** Evaluator used to compute values of nodes */
    private final Evaluator evaluator;

    /** Error listener */
    private final ErrorHandler errorHandler;

    /** Property map {@code name -> value} */
    private final Map<String, Object> properties = Maps.newHashMap();

    /**
     * Creates new {@link PropertyCollector} using specified node evaluator and
     * error handler.
     * 
     * @param evaluator
     *            Node evaluator
     * @param errorHandler
     *            Error handler
     */
    public PropertyCollector(Evaluator evaluator, ErrorHandler errorHandler) {
        this.evaluator = evaluator;
        this.errorHandler = errorHandler;
    }

    /**
     * Evaluates the specified node's value, and stores {@code (name, value)}
     * pair in the internal property map. All the potential exceptions are not
     * propagated, but forwarded to {@link ErrorHandler}.
     * 
     * @param node
     *            Property node to evaluate and store
     */
    public void consume(PropertyNode node) {
        String name = node.getName();
        try {
            Object value = evaluator.evaluate(node.getValue());
            if (!properties.containsKey(name)) {
                properties.put(name, value);
            } else {
                throw new PropertyRedefinitionException(name);
            }
        } catch (ConfigException e) {
            errorHandler.error(e);
        }
    }

    /**
     * Consumes all the nodes contained in the specified {@link Iterable}.
     * 
     * @param nodes
     *            Nodes to consume
     * @see #consume(PropertyNode)
     */
    public void consume(Iterable<PropertyNode> nodes) {
        for (PropertyNode node : nodes) {
            consume(node);
        }
    }

    /**
     * @return Immutable view of the internal property map
     */
    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

}
