package pl.edu.agh.ki.grieg.processing.core.config.xml;

import pl.edu.agh.ki.grieg.processing.core.config.evaluator.XmlConfigException;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

public class InvalidNodeException extends XmlConfigException {
    
    private final Element node;
    
    public InvalidNodeException(Element node) {
        super(formatMessage(node));
        this.node = node;
    }
    
    public Element getNode() {
        return node;
    }

    private static String formatMessage(Element node) {
        return String.format("Node <%s> is invalid in this context", node);
    }

}
