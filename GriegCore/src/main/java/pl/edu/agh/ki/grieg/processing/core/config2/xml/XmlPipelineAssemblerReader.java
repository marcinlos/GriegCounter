package pl.edu.agh.ki.grieg.processing.core.config2.xml;

import pl.edu.agh.ki.grieg.processing.core.config.Context;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineAssemblerNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineNode;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

public class XmlPipelineAssemblerReader implements Reader<PipelineNode> {

    public XmlPipelineAssemblerReader() {
        // empty
    }
    
    public PipelineAssemblerNode read(Element root, Context ctx) {
        String className = root.attr("class");
        return new PipelineAssemblerNode(className);
    }

}
