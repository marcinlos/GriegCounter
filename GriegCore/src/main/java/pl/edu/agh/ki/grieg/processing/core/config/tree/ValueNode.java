package pl.edu.agh.ki.grieg.processing.core.config.tree;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.ValueVisitor;

public interface ValueNode {
    
    void accept(ValueVisitor visitor) throws ConfigException;

}
