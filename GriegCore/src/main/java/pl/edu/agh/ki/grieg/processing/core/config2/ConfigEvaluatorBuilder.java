package pl.edu.agh.ki.grieg.processing.core.config2;

public class ConfigEvaluatorBuilder {

    private Evaluator evaluator;

    private PipelineEvaluator pipelineEvaluator;

    private ErrorHandler handler;

    public ConfigEvaluatorBuilder setEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public ConfigEvaluatorBuilder setPipelineEvaluator(
            PipelineEvaluator pipelineEvaluator) {
        this.pipelineEvaluator = pipelineEvaluator;
        return this;
    }
    
    public ConfigEvaluatorBuilder setErrorHandler(ErrorHandler handler) {
        this.handler = handler;
        return this;
    }

    public ConfigEvaluator build() {
        return new ConfigEvaluator(evaluator, pipelineEvaluator, handler);
    }

    public ConfigEvaluatorBuilder registerQualifier(String qualifier,
            ContentHandler<?> handler) {
        return this;
    }

}
