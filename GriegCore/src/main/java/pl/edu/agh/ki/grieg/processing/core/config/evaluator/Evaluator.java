package pl.edu.agh.ki.grieg.processing.core.config.evaluator;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.tree.ValueNode;

/**
 * Interface of value node evaluation functionality provider.
 * 
 * @author los
 */
public interface Evaluator {

    /**
     * Evaluates the specified value node.
     * 
     * @param node
     *            Node to evaluate
     * @return Value of the node
     * @throws ConfigException
     *             If evaluation of the node was impossible
     */
    Object evaluate(ValueNode node) throws ConfigException;

}
