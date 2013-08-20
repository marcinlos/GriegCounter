package pl.edu.agh.ki.grieg.processing.core.config2;

import pl.edu.agh.ki.grieg.processing.core.PipelineAssembler;
import pl.edu.agh.ki.grieg.processing.core.config.AssemblerDefinition;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ConfigNode;
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

    private final ErrorHandler handler;

    public ConfigEvaluator(Evaluator evaluator, ErrorHandler handler) {
        this.evaluator = evaluator;
        this.handler = handler;
    }

    public Config evaluate(ConfigNode node) throws ConfigException {
        PropertyCollector collector = new PropertyCollector(evaluator, handler);
        collector.consume(node.getPropertyNodes());
        
        Properties properties = PropertyMap.copyOf(collector.getProperties());
        return new Config(new AssemblerDefinition() {
            @Override
            public PipelineAssembler createAssembler() throws ConfigException {
                return null;
            }
        }, properties);
    }

}
