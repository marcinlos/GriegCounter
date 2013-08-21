package pl.edu.agh.ki.grieg.processing.core.config.xml;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.Context;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.XmlConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PipelineNode;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PipelinePartNode;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

public class PipelineReader implements Reader<PipelineNode> {

    private class NodeTransformer implements Function<Element, PipelinePartNode> {
        private final Context ctx;

        public NodeTransformer(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        public PipelinePartNode apply(Element input) {
            try {
                return process(input, ctx);
            } catch (ConfigException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private final Reader<? extends PipelinePartNode> elementReader;
    private final Reader<? extends PipelinePartNode> assemblerReader;

    public PipelineReader(Reader<? extends PipelinePartNode> elementReader,
            Reader<? extends PipelinePartNode> assemblerReader) {
        this.elementReader = elementReader;
        this.assemblerReader = assemblerReader;
    }

    @Override
    public PipelineNode read(Element node, Context ctx)
            throws ConfigException {
        try {
            List<PipelinePartNode> children = Lists.transform(node.children(),
                    new NodeTransformer(ctx));

            return new PipelineNode(children);
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ConfigException) {
                throw (ConfigException) cause;
            } else {
                throw e;
            }
        }
    }

    private PipelinePartNode process(Element element, Context ctx)
            throws ConfigException {
        if (element.name().equals("node")) {
            return elementReader.read(element, ctx);
        } else if (element.name().equals("assembler")) {
            return assemblerReader.read(element, ctx);
        } else {
            String message = "Unknown pipeline definition: " + element;
            XmlConfigException e = new XmlConfigException(message);
            throw new RuntimeException(e);
        }
    }

}
