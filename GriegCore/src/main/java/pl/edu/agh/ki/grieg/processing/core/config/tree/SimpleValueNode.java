package pl.edu.agh.ki.grieg.processing.core.config.tree;

import com.google.common.base.Objects;

public abstract class SimpleValueNode implements ValueNode {
    
    private final String value;

    public SimpleValueNode(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof SimpleValueNode) {
            SimpleValueNode other = (SimpleValueNode) o;
            return Objects.equal(value, other.value);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

}
