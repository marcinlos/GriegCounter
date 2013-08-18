package pl.edu.agh.ki.grieg.processing.core.config2;

import pl.edu.agh.ki.grieg.processing.core.config2.tree.CompleteValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ComplexValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ConvertibleValueNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PrimitiveValueNode;

public interface ValueVisitor {
    
    void visit(CompleteValueNode node);
    
    void visit(ComplexValueNode<?> node);
    
    void visit(ConvertibleValueNode node);
    
    void visit(PrimitiveValueNode node);
    
}
