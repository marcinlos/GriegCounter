package pl.edu.agh.ki.grieg.processing.core.config2.tree;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

public class ComplexValueNode implements ValueNode {
    
    private final Object content;
    
    private final String qualifier;
    
    public ComplexValueNode(Object content, String qualifier) {
        this.content = checkNotNull(content);
        this.qualifier = checkNotNull(qualifier);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ComplexValueNode) {
            ComplexValueNode other = (ComplexValueNode) o;
            return content.equals(other.content) 
                    && qualifier.equals(other.qualifier);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(content, qualifier);
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("qualifier", qualifier)
                .add("content", content)
                .toString();
    }

}
