package pl.edu.agh.ki.grieg.processing.core.config2;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.CompleteValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ComplexValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ConvertibleValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PrimitiveValueNode;

public interface ValueVisitor {
    
    void visit(CompleteValueNode node) throws ConfigException;
    
    void visit(ComplexValueNode<?> node) throws ConfigException;
    
    void visit(ConvertibleValueNode node) throws ConfigException;
    
    void visit(PrimitiveValueNode node) throws ConfigException;
    
}
