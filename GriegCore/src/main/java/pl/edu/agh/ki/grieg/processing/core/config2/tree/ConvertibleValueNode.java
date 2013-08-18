package pl.edu.agh.ki.grieg.processing.core.config2.tree;

import static com.google.common.base.Preconditions.checkNotNull;
import pl.edu.agh.ki.grieg.processing.core.config2.ValueVisitor;

import com.google.common.base.Objects;

public class ConvertibleValueNode extends SimpleValueNode {

    private final String type;

    public ConvertibleValueNode(String value, String type) {
        super(value);
        this.type = checkNotNull(type);
    }
    
    public String getType() {
        return type;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof ConvertibleValueNode) {
            ConvertibleValueNode other = (ConvertibleValueNode) o;
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
    
    @Override
    public void accept(ValueVisitor visitor) {
        visitor.visit(this);
    }

}
