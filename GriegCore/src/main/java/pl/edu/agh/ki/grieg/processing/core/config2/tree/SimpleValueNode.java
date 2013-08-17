package pl.edu.agh.ki.grieg.processing.core.config2.tree;

public abstract class SimpleValueNode implements ValueNode {
    
    private final String value;

    public SimpleValueNode(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }

}
