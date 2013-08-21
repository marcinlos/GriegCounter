package pl.edu.agh.ki.grieg.processing.core.config2;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ValueNode;
import pl.edu.agh.ki.grieg.util.converters.Converter;

public class DefaultEvaluator implements Evaluator {

    private final Converter converter;

    private final ContentHandlerProvider handlers;

    public DefaultEvaluator(Converter converter, ContentHandlerProvider handlers) {
        this.converter = converter;
        this.handlers = handlers;
    }

    @Override
    public Object evaluate(ValueNode node) throws ConfigException {
        EvaluatingVisitor visitor = new EvaluatingVisitor(converter, handlers);
        node.accept(visitor);
        return visitor.getValue();
    }

}
