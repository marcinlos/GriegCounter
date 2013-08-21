package pl.edu.agh.ki.grieg.processing.core.config.tree;

import static com.google.common.base.Preconditions.checkNotNull;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.ValueVisitor;

import com.google.common.base.Objects;

public class ComplexValueNode<T> implements ValueNode {
    
    private final T content;
    
    private final String qualifier;
    
    public ComplexValueNode(T content, String qualifier) {
        this.content = checkNotNull(content);
        this.qualifier = checkNotNull(qualifier);
    }
    
    public static <T> ComplexValueNode<T> of(T content, String qualifier) {
        return new ComplexValueNode<T>(content, qualifier);
    }

    public T getContent() {
        return content;
    }
    
    public String getQualifier() {
        return qualifier;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof ComplexValueNode) {
            ComplexValueNode<?> other = (ComplexValueNode<?>) o;
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
    
    @Override
    public void accept(ValueVisitor visitor) throws ConfigException {
        visitor.visit(this);
    }

}
