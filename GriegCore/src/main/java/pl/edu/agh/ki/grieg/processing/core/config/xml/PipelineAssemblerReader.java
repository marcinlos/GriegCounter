package pl.edu.agh.ki.grieg.processing.core.config.xml;

import pl.edu.agh.ki.grieg.processing.core.config.Context;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PipelineAssemblerNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PipelinePartNode;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

public class PipelineAssemblerReader implements Reader<PipelinePartNode> {

    public PipelineAssemblerReader() {
        // empty
    }
    
    public PipelineAssemblerNode read(Element root, Context ctx) {
        String className = root.attr("class");
        return new PipelineAssemblerNode(className);
    }

}
