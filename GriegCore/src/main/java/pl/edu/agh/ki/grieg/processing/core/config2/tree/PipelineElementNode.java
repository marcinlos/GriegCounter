package pl.edu.agh.ki.grieg.processing.core.config2.tree;

import static com.google.common.base.Preconditions.checkNotNull;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.PipelineVisitor;

import com.google.common.base.Objects;

public class PipelineElementNode implements PipelineNode {

    private final String name;

    private final String type;

    private final String source;

    public PipelineElementNode(String name, String type, String source) {
        this.name = checkNotNull(name);
        this.type = checkNotNull(type);
        this.source = checkNotNull(source);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PipelineElementNode) {
            PipelineElementNode other = (PipelineElementNode) o;
            return Objects.equal(name, other.name)
                    && Objects.equal(type, other.type)
                    && Objects.equal(source, other.source);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, type, source);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", name)
                .add("type", type)
                .add("source", source)
                .toString();
    }

    @Override
    public void accept(PipelineVisitor visitor) throws ConfigException {
        visitor.visit(this);
    }

}
