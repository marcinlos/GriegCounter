package pl.edu.agh.ki.grieg.processing.core.config2.xml;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.xml.XmlConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineNodeList;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

public class XmlPipelineReader implements Reader<PipelineNodeList> {

    private class NodeTransformer implements Function<Element, PipelineNode> {
        @Override
        public PipelineNode apply(Element input) {
            try {
                return process(input);
            } catch (ConfigException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private final Reader<? extends PipelineNode> elementReader;
    private final Reader<? extends PipelineNode> assemblerReader;

    public XmlPipelineReader(Reader<? extends PipelineNode> elementReader,
            Reader<? extends PipelineNode> assemblerReader) {
        this.elementReader = elementReader;
        this.assemblerReader = assemblerReader;
    }

    @Override
    public PipelineNodeList read(Element node) throws ConfigException {
        try {
            List<PipelineNode> children = Lists.transform(node.children(),
                    new NodeTransformer());

            return new PipelineNodeList(children);
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ConfigException) {
                throw (ConfigException) cause;
            } else {
                throw e;
            }
        }
    }

    private PipelineNode process(Element element) throws ConfigException {
        if (element.name().equals("node")) {
            return elementReader.read(element);
        } else if (element.name().equals("assembler")) {
            return assemblerReader.read(element);
        } else {
            String message = "Unknown pipeline definition: " + element;
            XmlConfigException e = new XmlConfigException(message);
            throw new RuntimeException(e);
        }
    }

}
