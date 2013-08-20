package pl.edu.agh.ki.grieg.processing.core.config2.tree;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

public class PipelineNode {
    
    private final List<PipelinePartNode> elements;
    
    public PipelineNode(PipelinePartNode...elements) {
        this(Arrays.asList(elements));
    }

    public PipelineNode(Iterable<? extends PipelinePartNode> elements) {
        this.elements = ImmutableList.copyOf(elements);
    }
    
    public List<PipelinePartNode> getElements() {
        return elements;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof PipelineNode) {
            PipelineNode other = (PipelineNode) o;
            return Objects.equal(elements, other.elements);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return elements.hashCode();
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("elements", elements)
                .toString();
    }
    
}
 