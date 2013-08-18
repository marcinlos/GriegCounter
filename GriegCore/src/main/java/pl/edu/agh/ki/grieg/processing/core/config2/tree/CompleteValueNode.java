package pl.edu.agh.ki.grieg.processing.core.config2.tree;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.ValueVisitor;

import com.google.common.base.Objects;

public class CompleteValueNode implements ValueNode {
    
    private final Object value;

    public CompleteValueNode(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof CompleteValueNode) {
            CompleteValueNode other = (CompleteValueNode) o;
            return Objects.equal(value, other.value);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("value", value)
                .toString();
    }

    @Override
    public void accept(ValueVisitor visitor) throws ConfigException {
        visitor.visit(this);
    }
}
