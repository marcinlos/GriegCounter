package pl.edu.agh.ki.grieg.processing.core.config.xml;

import com.google.common.base.Objects;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.Context;
import pl.edu.agh.ki.grieg.processing.core.config.tree.CompleteValueNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.ComplexValueNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.ConvertibleValueNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PrimitiveValueNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PropertyNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.ValueNode;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

public class PropertyReader implements Reader<PropertyNode> {

    public PropertyReader() {
        // empty
    }

    @Override
    public PropertyNode read(Element node, Context ctx) throws ConfigException {
        String name = node.attr("name");
        ValueNode value = extractValue(node, ctx);
        return new PropertyNode(name, value);
    }

    private ValueNode extractValue(Element node, Context ctx)
            throws ConfigException {
        String nodeName = node.name();
        String value = node.val();
        if (node.ns().equals(ConfigTreeReader.NS)) {
            Class<?> type = Reflection.primitiveForName(nodeName);
            if (type != null) {
                return new PrimitiveValueNode(value, type);
            } else if (nodeName.equals("string")) {
                return new CompleteValueNode(value);
            } else if (nodeName.equals("value")) {
                String valueType = node.attr("type");
                return new ConvertibleValueNode(value, valueType);
            } else {
                throw new InvalidNodeException(node);
            }
        } else {
            String qualifier = Objects.firstNonNull(node.ns(), "[global]");
            return ComplexValueNode.of(node, qualifier);
        }
    }

}
