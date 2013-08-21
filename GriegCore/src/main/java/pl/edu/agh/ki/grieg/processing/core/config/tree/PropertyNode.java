package pl.edu.agh.ki.grieg.processing.core.config.tree;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

public final class PropertyNode {
    
    private final String name;
    
    private final ValueNode value;

    public PropertyNode(String name, ValueNode value) {
        this.name = checkNotNull(name);
        this.value = checkNotNull(value);
    }
    
    public String getName() {
        return name;
    }
    
    public ValueNode getValue() {
        return value;
    }

    
    @Override
    public boolean equals(Object o) {
        if (o instanceof PropertyNode) {
            PropertyNode other = (PropertyNode) o;
            return name.equals(other.name)
                    && value.equals(other.value);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(name, value);
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", name)
                .add("value", value)
                .toString();
    }
    
}
