package pl.edu.agh.ki.grieg.processing.core.config2.tree;

import pl.edu.agh.ki.grieg.processing.core.config2.PipelineVisitor;

public interface PipelineNode {

    void accept(PipelineVisitor visitor);
    
}
