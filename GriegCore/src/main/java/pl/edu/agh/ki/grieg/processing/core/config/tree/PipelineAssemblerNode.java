package pl.edu.agh.ki.grieg.processing.core.config.tree;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.PipelineVisitor;

import com.google.common.base.Objects;

public class PipelineAssemblerNode implements PipelinePartNode {

    private final String className;
    
    public PipelineAssemblerNode(String className) {
        this.className = className;
    }
    
    public String getClassName() {
        return className;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof PipelineAssemblerNode) {
            PipelineAssemblerNode other = (PipelineAssemblerNode) o;
            return Objects.equal(className, other.className);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(className);
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("class", className)
                .toString();
    }
    
    @Override
    public void accept(PipelineVisitor visitor) throws ConfigException {
        visitor.visit(this);
    }

}
