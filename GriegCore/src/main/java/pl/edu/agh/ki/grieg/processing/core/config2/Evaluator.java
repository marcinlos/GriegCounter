package pl.edu.agh.ki.grieg.processing.core.config2;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ValueNode;

public interface Evaluator {
    
    Object evaluate(ValueNode node) throws ConfigException;

}
