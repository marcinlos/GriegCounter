package pl.edu.agh.ki.grieg.processing.core.config2.tree;

import com.google.common.base.Objects;

public class PrimitiveValueNode extends SimpleValueNode {
    
    private final Class<?> type;

    public PrimitiveValueNode(String value, Class<?> type) {
        super(value);
        this.type = type;
    }
    
    public Class<?> getType() {
        return type;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof CompleteValueNode) {
            PrimitiveValueNode other = (PrimitiveValueNode) o;
            return super.equals(o) && type.equals(other.type);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), type);
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("type", getType())
                .add("value", getValue())
                .toString();
    }
    
}
