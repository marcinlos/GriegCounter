package pl.edu.agh.ki.grieg.processing.core.config2;

import static com.google.common.base.Preconditions.checkNotNull;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.CompleteValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ComplexValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ConvertibleValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PrimitiveValueNode;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.ReflectionException;
import pl.edu.agh.ki.grieg.util.converters.ConversionException;
import pl.edu.agh.ki.grieg.util.converters.Converter;

import com.google.common.reflect.TypeToken;

public class EvaluatingVisitor implements ValueVisitor {

    private Object value;

    private final Converter converter;

    private final ContentHandlerProvider handlers;

    public EvaluatingVisitor(Converter converter,
            ContentHandlerProvider handlers) {
        this.converter = checkNotNull(converter);
        this.handlers = checkNotNull(handlers);
    }

    public <T> T getValue(Class<T> clazz) {
        Class<T> wrapped = Reflection.wrap(clazz);
        return wrapped.cast(value);
    }

    public Object getValue() {
        return value;
    }

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

    @Override
    public void visit(CompleteValueNode node) throws ConfigException {
        value = node.getValue();
    }

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

    private void convert(String literal, Class<?> clazz) throws ValueException {
        try {
            Class<?> type = Reflection.wrap(clazz);
            value = converter.convert(literal, TypeToken.of(type));
        } catch (ConversionException e) {
            throw new ValueException("Conversion failure", e);
        }
    }

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

    @Override
    public void visit(PrimitiveValueNode node) throws ConfigException {
        Class<?> type = node.getType();
        String literal = node.getValue();
        convert(literal, type);
    }

}
