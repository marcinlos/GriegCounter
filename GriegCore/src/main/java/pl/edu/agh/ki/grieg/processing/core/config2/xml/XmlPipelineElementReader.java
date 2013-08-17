package pl.edu.agh.ki.grieg.processing.core.config2.xml;

import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineElementNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineNode;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

public class XmlPipelineElementReader implements Reader<PipelineNode> {

    public PipelineElementNode read(Element node) {
        String name = node.child("name").val();
        String type = node.child("class").val();
        String source = node.child("source").val();
        return new PipelineElementNode(name, type, source);
    }

}
