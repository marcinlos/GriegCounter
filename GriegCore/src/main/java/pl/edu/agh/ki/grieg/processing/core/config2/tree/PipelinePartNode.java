package pl.edu.agh.ki.grieg.processing.core.config2.tree;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.PipelineVisitor;

public interface PipelinePartNode {

    void accept(PipelineVisitor visitor) throws ConfigException;
    
}