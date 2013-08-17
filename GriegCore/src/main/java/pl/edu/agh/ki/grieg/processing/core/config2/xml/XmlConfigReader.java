package pl.edu.agh.ki.grieg.processing.core.config2.xml;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.ConfigNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineNodeList;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PropertyNode;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

public class XmlConfigReader implements Reader<ConfigNode> {

    public static final String NS =
            "https://case.iisg.agh.edu.pl/confluence/display/prpj13kpGriegCounter";

    private final Reader<PropertyNode> propertyReader;

    private final Reader<PipelineNodeList> pipelineReader;

    public XmlConfigReader(Reader<PropertyNode> propertyReader,
            Reader<PipelineNodeList> pipelineReader) {
        this.propertyReader = propertyReader;
        this.pipelineReader = pipelineReader;
    }

    @Override
    public ConfigNode read(Element root) throws ConfigException {
        Element pipelineNode = root.child("pipeline");
        Element propertiesNode = root.child("properties");

        for (Element node : propertiesNode.children()) {
            
        }
        
        PipelineNodeList pipeline = pipelineReader.read(pipelineNode);

//        return new ConfigNode(properties, pipeline);
        return null;
    }

}
