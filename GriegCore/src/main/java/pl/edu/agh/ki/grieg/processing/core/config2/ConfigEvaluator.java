package pl.edu.agh.ki.grieg.processing.core.config2;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import pl.edu.agh.ki.grieg.processing.core.config.AssemblerDefinition;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ConfigNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PropertyNode;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.PropertyMap;

/**
 * Top-level class evaluating the whole configuration tree and transforming it
 * into ready to use version.
 * 
 * @author los
 */
public class ConfigEvaluator {

    private final Evaluator evaluator;
    
    private final PipelineEvaluator pipelineEvaluator;

    private final ErrorHandler handler;

    public ConfigEvaluator(Evaluator evaluator,
            PipelineEvaluator pipelineEvaluator, ErrorHandler handler) {
        this.evaluator = checkNotNull(evaluator);
        this.pipelineEvaluator = checkNotNull(pipelineEvaluator);
        this.handler = handler;
    }

    public Config evaluate(ConfigNode node) throws ConfigException {
        List<PropertyNode> propertiesNode = node.getPropertyNodes();
        PropertyCollector collector = new PropertyCollector(evaluator, handler);
        collector.consume(propertiesNode);
        Properties properties = PropertyMap.copyOf(collector.getProperties());

        PipelineNode pipelineNode = node.getPipelineNode();
        AssemblerDefinition assembler = pipelineEvaluator.evaluate(pipelineNode);
        
        return new Config(assembler, properties);
    }

}
