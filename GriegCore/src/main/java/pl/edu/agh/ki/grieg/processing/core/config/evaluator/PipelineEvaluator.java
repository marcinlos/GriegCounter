package pl.edu.agh.ki.grieg.processing.core.config.evaluator;

import pl.edu.agh.ki.grieg.processing.core.DefaultPipelineAssembler;
import pl.edu.agh.ki.grieg.processing.core.PipelineAssembler;
import pl.edu.agh.ki.grieg.processing.core.config.AssemblerDefinition;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PipelineNode;

public class PipelineEvaluator {
    
    public AssemblerDefinition evaluate(PipelineNode node) {
        return new AssemblerDefinition() {
            @Override
            public PipelineAssembler createAssembler() throws ConfigException {
                return new DefaultPipelineAssembler();
            }
        };
    }

}
