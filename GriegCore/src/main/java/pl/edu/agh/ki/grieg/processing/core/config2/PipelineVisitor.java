package pl.edu.agh.ki.grieg.processing.core.config2;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineAssemblerNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineElementNode;

public interface PipelineVisitor {
    
    void visit(PipelineElementNode node) throws ConfigException;
    
    void visit(PipelineAssemblerNode node) throws ConfigException;

}
