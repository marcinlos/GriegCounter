package pl.edu.agh.ki.grieg.processing.core.config.tree;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.PipelineVisitor;

public interface PipelinePartNode {

    void accept(PipelineVisitor visitor) throws ConfigException;
    
}
