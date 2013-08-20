package pl.edu.agh.ki.grieg.processing.core.config2.tree;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.common.base.Objects;

public class ConfigNode {

    private final List<PropertyNode> propertyNodes;

    private final PipelineNode pipelineNodes;

    public ConfigNode(List<PropertyNode> propertyNodes,
            PipelineNode pipelineNodes) {
        this.propertyNodes = checkNotNull(propertyNodes);
        this.pipelineNodes = checkNotNull(pipelineNodes);
    }

    public List<PropertyNode> getPropertyNodes() {
        return propertyNodes;
    }

    public PipelineNode getPipelineNodes() {
        return pipelineNodes;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ConfigNode) {
            ConfigNode other = (ConfigNode) o;
            return propertyNodes.equals(other.propertyNodes)
                    && pipelineNodes.equals(other.pipelineNodes);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(propertyNodes, pipelineNodes);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("props", propertyNodes)
                .add("pipeline", pipelineNodes)
                .toString();
    }

}
