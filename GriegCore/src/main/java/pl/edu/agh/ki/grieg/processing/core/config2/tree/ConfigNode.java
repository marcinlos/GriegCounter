package pl.edu.agh.ki.grieg.processing.core.config2.tree;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.google.common.base.Objects;

public class ConfigNode {
    
    private final Map<String, PropertyNode> propertyNodes;
    
    private final PipelineNodeList pipelineNodes;

    public ConfigNode(Map<String, PropertyNode> propertyNodes,
            PipelineNodeList pipelineNodes) {
        this.propertyNodes = checkNotNull(propertyNodes);
        this.pipelineNodes = checkNotNull(pipelineNodes);
    }
    
    public Map<String, PropertyNode> getPropertyNodes() {
        return propertyNodes;
    }
    
    public PipelineNodeList getPipelineNodes() {
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
    
}
