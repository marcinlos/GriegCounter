package pl.edu.agh.ki.grieg.processing.core.config2.tree;

import pl.edu.agh.ki.grieg.processing.core.config2.ValueVisitor;

public interface ValueNode {
    
    void accept(ValueVisitor visitor);

}
