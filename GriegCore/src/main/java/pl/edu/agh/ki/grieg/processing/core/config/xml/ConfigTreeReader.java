package pl.edu.agh.ki.grieg.processing.core.config.xml;

import java.util.List;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.Context;
import pl.edu.agh.ki.grieg.processing.core.config.tree.ConfigNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PipelineNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PropertyNode;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

import com.google.common.collect.Lists;

public class ConfigTreeReader implements Reader<ConfigNode> {

    public static final String NS =
            "https://case.iisg.agh.edu.pl/confluence/display/prpj13kpGriegCounter";

    private final Reader<PropertyNode> propertyReader;

    private final Reader<PipelineNode> pipelineReader;

    public ConfigTreeReader(Reader<PropertyNode> propertyReader,
            Reader<PipelineNode> pipelineReader) {
        this.propertyReader = propertyReader;
        this.pipelineReader = pipelineReader;
    }

    @Override
    public ConfigNode read(Element root, Context ctx) throws ConfigException {

        List<PropertyNode> properties = Lists.newArrayList();
        Element propertiesNode = root.child("properties");
        if (propertiesNode != null) {
            for (Element node : propertiesNode.children()) {
                PropertyNode prop = propertyReader.read(node, ctx);
                properties.add(prop);
            }
        }

        Element pipelineNode = root.child("pipeline");
        PipelineNode pipeline = pipelineReader.read(pipelineNode, ctx);

        return new ConfigNode(properties, pipeline);
    }

}
