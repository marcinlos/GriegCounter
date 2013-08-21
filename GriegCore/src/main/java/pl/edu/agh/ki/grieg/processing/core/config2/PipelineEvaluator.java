package pl.edu.agh.ki.grieg.processing.core.config2;

import pl.edu.agh.ki.grieg.processing.core.DefaultPipelineAssembler;
import pl.edu.agh.ki.grieg.processing.core.PipelineAssembler;
import pl.edu.agh.ki.grieg.processing.core.config.AssemblerDefinition;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineNode;

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
