package pl.edu.agh.ki.grieg.processing.core.config.xml;

import pl.edu.agh.ki.grieg.processing.core.config.Context;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PipelineElementNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PipelinePartNode;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

public class PipelineElementReader implements Reader<PipelinePartNode> {

    public PipelineElementNode read(Element node, Context ctx) {
        String name = node.child("name").val();
        String type = node.child("class").val();
        String source = node.child("source").val();
        return new PipelineElementNode(name, type, source);
    }

}
