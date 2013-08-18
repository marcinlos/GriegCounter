package pl.edu.agh.ki.grieg.processing.core.config2.tree;

import com.google.common.base.Objects;

public class PipelineAssemblerNode implements PipelineNode {

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

}