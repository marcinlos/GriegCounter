package pl.edu.agh.ki.grieg.processing.core.config.evaluator;

import static com.google.common.base.Preconditions.checkNotNull;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.tree.CompleteValueNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.ComplexValueNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.ConvertibleValueNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PrimitiveValueNode;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.ReflectionException;
import pl.edu.agh.ki.grieg.util.converters.ConversionException;
import pl.edu.agh.ki.grieg.util.converters.Converter;

import com.google.common.reflect.TypeToken;

/**
 * Value node visitor whose purpose is to calculate actual value this node
 * represents. Uses supplied {@link Converter} to evaluate nodes with flat
 * structure (value represented by text), and {@link ContentHandlerProvider} to
 * pass complex nodes to appropriate handlers.
 * 
 * <p>
 * Result of the node evaluation can be retrieved using {@link #getValue()} (or
 * {@link #getValue(Class)} for additional type-safety). Results of calling
 * these functions before this visitor has been accepted by some node are
 * unspecified. As it's stateful, visitor is supposed to be used once only.
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * ValueNode node = ...
 * EvaluatingVisitor visitor = new EvaluatingVisitor(converter, handlers);
 * node.accept(visitor);
 * String value = visitor.getValue(String.class);
 * </pre>
 * 
 * @author los
 */
public class EvaluatingVisitor implements ValueVisitor {

    /** Value of the visited node */
    private Object value;

    /** Converter used to convert text literals to arbitrary values */
    private final Converter converter;

    /** Mapping qualifier -> {@link ContentHandler} */
    private final ContentHandlerProvider handlers;

    /**
     * Creates new {@link EvaluatingVisitor} using specified {@link Converter}
     * and {@link ContentHandlerProvider} to compute node values.
     * 
     * @param converter
     *            {@link Converter} used to convert string literals
     * @param handlers
     *            {@link ContentHandlerProvider} used to retrieve
     *            {@link ContentHandler}s
     */
    public EvaluatingVisitor(Converter converter,
            ContentHandlerProvider handlers) {
        this.converter = checkNotNull(converter);
        this.handlers = checkNotNull(handlers);
    }

    /**
     * Returns value of the previously visited node cast to the specified type.
     * Primitive types may be used, e.g. {@code getValue(int.class)}.
     * 
     * <p>
     * Note: should not be called before actually visiting some node.
     * 
     * @param clazz
     *            Expected type of the node
     * @return Value of the node cast to the specified type
     * @see #getValue()
     */
    public <T> T getValue(Class<T> clazz) {
        Class<T> wrapped = Reflection.wrap(clazz);
        return wrapped.cast(getValue());
    }

    /**
     * Returns the value of the visited node.
     * 
     * <p>
     * Node: should not be called before actually visiting some node.
     * 
     * @return Value of the previously visited node
     * @see #getValue(Class)
     */
    public Object getValue() {
        return value;
    }

    /**
     * Retrieves {@link ContentHandler} for the specified qualifier, or throws
     * {@link NoHandlerException} if there is none.
     * 
     * @param qualifier
     *            Qualifier to lookup
     * @return {@link ContentHandler} associated with this qualifier
     * @throws NoHandlerException
     *             If there is no {@link ContentHandler} associated with this
     *             qualifier
     */
    @SuppressWarnings("unchecked")
    private ContentHandler<Object> getHandlerForQualifier(String qualifier)
            throws NoHandlerException {
        ContentHandler<?> handler = handlers.forQualifier(qualifier);
        if (handler != null) {
            // really, really unsafe, but genericity makes it look nicer from
            // the outside...
            return (ContentHandler<Object>) handler;
        } else {
            throw new NoHandlerException(qualifier);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(CompleteValueNode node) throws ConfigException {
        value = node.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(ComplexValueNode<?> node) throws ConfigException {
        String qualifier = node.getQualifier();
        Object content = node.getContent();
        ContentHandler<Object> handler = getHandlerForQualifier(qualifier);
        try {
            value = handler.evaluate(content);
        } catch (Exception e) {
            throw new ContentHandlerException(content, e);
        }
    }

    /**
     * Converts string literal to value of the specified type. Primitive type
     * classes (e.g. {@code int.class}) are replaced by appropriate wrappers.
     * 
     * @param literal
     *            String literal to convert
     * @param clazz
     *            Type to cast the value to
     * @throws ValueException
     *             If the conversion fails
     */
    private void convert(String literal, Class<?> clazz) throws ValueException {
        try {
            value = converter.convert(literal, TypeToken.of(clazz));
        } catch (ConversionException e) {
            throw new ValueException("Conversion failure", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(ConvertibleValueNode node) throws ConfigException {
        String type = node.getType();
        String literal = node.getValue();
        try {
            Class<?> clazz = Reflection.getClass(type);
            convert(literal, clazz);
        } catch (ReflectionException e) {
            String msg = "Problem with specified type [" + type + "]";
            throw new ValueException(msg, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(PrimitiveValueNode node) throws ConfigException {
        Class<?> type = node.getType();
        String literal = node.getValue();
        convert(literal, type);
    }

}
