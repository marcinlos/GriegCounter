package pl.edu.agh.ki.grieg.processing.core.config2;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.CompleteValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ComplexValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ConvertibleValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PrimitiveValueNode;

/**
 * Generic visitor for traversing value nodes.
 * 
 * @author los
 */
public interface ValueVisitor {

    /**
     * Operates on nodes describing already evaluated expressions.
     * 
     * @param node
     *            Value node
     * @throws ConfigException
     *             If the operation fails
     */
    void visit(CompleteValueNode node) throws ConfigException;

    /**
     * Operates on nodes describing complex values.
     * 
     * @param node
     *            Value node
     * @throws ConfigException
     *             if the operation fails
     */
    void visit(ComplexValueNode<?> node) throws ConfigException;

    /**
     * Operates on nodes describing simple (type, text) nodes.
     * 
     * @param node
     *            Value node
     * @throws ConfigException
     *             if the operation fails
     */
    void visit(ConvertibleValueNode node) throws ConfigException;

    /**
     * Operates on nodes describing simple values with statically determined
     * types (i.e. with no runtime lookup of the type).
     * 
     * @param node
     *            Value node
     * @throws ConfigException
     *             if the operation fails
     */
    void visit(PrimitiveValueNode node) throws ConfigException;

}
