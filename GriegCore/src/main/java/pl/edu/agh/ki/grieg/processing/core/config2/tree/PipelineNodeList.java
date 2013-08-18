package pl.edu.agh.ki.grieg.processing.core.config2.tree;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

public class PipelineNodeList implements PipelineNode {
    
    private final List<PipelineNode> elements;
    
    public PipelineNodeList(PipelineNode...elements) {
        this(Arrays.asList(elements));
    }

    public PipelineNodeList(Iterable<? extends PipelineNode> elements) {
        this.elements = ImmutableList.copyOf(elements);
    }
    
    public List<PipelineNode> getElements() {
        return elements;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof PipelineNodeList) {
            PipelineNodeList other = (PipelineNodeList) o;
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
 