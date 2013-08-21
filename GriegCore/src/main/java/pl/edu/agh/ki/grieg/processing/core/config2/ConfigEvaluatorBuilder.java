package pl.edu.agh.ki.grieg.processing.core.config2;

import pl.edu.agh.ki.grieg.util.converters.Converter;
import pl.edu.agh.ki.grieg.util.converters.ConverterMap;
import pl.edu.agh.ki.grieg.util.converters.Types;

import com.google.common.base.Predicate;
import com.google.common.reflect.TypeToken;

public class ConfigEvaluatorBuilder {

    private static final class EmptyErrorHandler implements ErrorHandler {
        @Override
        public void error(Throwable e) {
            // empty
        }
    }

    private ErrorHandler errorHandler;

    private Converter converter;

    private ContentHandlerProvider handlerProvider;

    private final ConverterMap converterMap;

    private final DefaultHandlerProvider defaultHandlerProvider;

    public ConfigEvaluatorBuilder() {
        converterMap = ConverterMap.newMap();
        defaultHandlerProvider = new DefaultHandlerProvider();

        converter = converterMap;
        handlerProvider = defaultHandlerProvider;
    }

    public ConfigEvaluatorBuilder set(Converter converter) {
        return this;
    }

    public ConfigEvaluatorBuilder set(ContentHandlerProvider handlers) {
        return this;
    }

    public ConfigEvaluatorBuilder set(ErrorHandler handler) {
        this.errorHandler = handler;
        return this;
    }

    public ConfigEvaluatorBuilder register(String qualifier,
            ContentHandler<?> handler) {
        return this;
    }

    public ConfigEvaluatorBuilder add(Class<?> type, Converter converter) {
        return add(Types.subclassOf(type), converter);
    }

    public ConfigEvaluatorBuilder add(
            Predicate<? super TypeToken<?>> criterion,
            Converter converter) {
        return this;
    }

    public ConfigEvaluator build() {
        if (errorHandler == null) {
            errorHandler = new EmptyErrorHandler();
        }
        Evaluator evaluator = new DefaultEvaluator(converter, handlerProvider);
        PipelineEvaluator pipelineEvaluator = new PipelineEvaluator();
        return new ConfigEvaluator(evaluator, pipelineEvaluator, errorHandler);
    }

}
