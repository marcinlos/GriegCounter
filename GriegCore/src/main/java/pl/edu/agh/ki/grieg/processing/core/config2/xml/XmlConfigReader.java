package pl.edu.agh.ki.grieg.processing.core.config2.xml;

import java.util.Map;

import com.google.common.collect.Maps;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.Context;
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
    public ConfigNode read(Element root, Context ctx) throws ConfigException {

        Map<String, PropertyNode> properties = Maps.newHashMap();
        Element propertiesNode = root.child("properties");
        if (propertiesNode != null) {
            for (Element node : propertiesNode.children()) {
                PropertyNode prop = propertyReader.read(node, ctx);
                properties.put(prop.getName(), prop);
            }
        }
        
        Element pipelineNode = root.child("pipeline");
        PipelineNodeList pipeline = pipelineReader.read(pipelineNode, ctx);

        return new ConfigNode(properties, pipeline);
    }

}
