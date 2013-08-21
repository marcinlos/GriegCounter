package pl.edu.agh.ki.grieg.processing.core.config.evaluator;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PipelineAssemblerNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PipelineElementNode;

public interface PipelineVisitor {
    
    void visit(PipelineElementNode node) throws ConfigException;
    
    void visit(PipelineAssemblerNode node) throws ConfigException;

}
