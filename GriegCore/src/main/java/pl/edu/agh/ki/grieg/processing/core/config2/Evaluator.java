package pl.edu.agh.ki.grieg.processing.core.config2;

import com.google.common.reflect.TypeToken;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.CompleteValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ComplexValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ConvertibleValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PrimitiveValueNode;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.ReflectionException;
import pl.edu.agh.ki.grieg.util.converters.ConversionException;
import pl.edu.agh.ki.grieg.util.converters.Converter;

public class Evaluator implements ValueVisitor {

    private Object value;

    private final Converter converter;

    public Evaluator(Converter converter) {
        this.converter = converter;
    }

    public <T> T getValue(Class<T> clazz) {
        Class<T> unwrapped = Reflection.wrap(clazz);
        return unwrapped.cast(value);
    }
    
    public Object getValue() {
        return value;
    }

    @Override
    public void visit(CompleteValueNode node) throws ConfigException {
        value = node.getValue();
    }

    @Override
    public void visit(ComplexValueNode<?> node) throws ConfigException {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ConvertibleValueNode node) throws ConfigException {
        String type = node.getType();
        String literal = node.getValue();
        try {
            Class<?> clazz = Reflection.getClass(type);
            Class<?> unwrapped = Reflection.wrap(clazz);
            value = converter.convert(literal, TypeToken.of(unwrapped));
        } catch (ReflectionException e) {
            String msg = "Problem with specified type [" + type + "]";
            throw new ValueException(msg, e);
        } catch (ConversionException e) {
            throw new ValueException(e);
        }
    }

    @Override
    public void visit(PrimitiveValueNode node) throws ConfigException {
        try {
            Class<?> type = Reflection.wrap(node.getType());
            String literal = node.getValue();
            value = converter.convert(literal, TypeToken.of(type));
        } catch (ConversionException e) {
            throw new ConfigException("Conversion failure", e);
        }
    }

}
