package pl.edu.agh.ki.grieg.processing.core.config2.xml;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.ki.grieg.processing.core.config.xml.XmlConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineAssemblerNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineElementNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineNodeList;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineNode;
import pl.edu.agh.ki.grieg.util.xml.XmlException;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

public class XmlPipelineReaderTest extends XmlReaderTest {

    private XmlPipelineReader reader;

    private PipelineNode[] nodes = {
            new PipelineElementNode("segmenter",
                    "pl.edu.agh.ki.grieg.analysis.Segmenter", "ROOT"),
            new PipelineElementNode("compressor",
                    "pl.edu.agh.ki.grieg.analysis.WaveCompressor", "segmenter"),
            new PipelineAssemblerNode(
                    "pl.edu.agh.ki.grieg.DefaultPipelineAssembler"),
            new PipelineElementNode("hamming",
                    "pl.edu.agh.ki.grieg.analysis.HammingSegmenter", "ROOT"),
            new PipelineAssemblerNode(
                    "pl.edu.agh.ki.grieg.SuperPipelineAssembler")
    };

    @Before
    public void setup() throws XmlException {
        Reader<PipelineNode> elementReader = new XmlPipelineElementReader();
        Reader<PipelineNode> assemblerReader = new XmlPipelineAssemblerReader();
        reader = new XmlPipelineReader(elementReader, assemblerReader);
    }

    @Test
    public void pipelineIsOk() throws Exception {
        PipelineNode node = new PipelineNodeList(nodes);
        assertEquals(node, reader.read(pipeline));
    }

    @Test(expected = XmlConfigException.class)
    public void failsWithInvalidData() throws Exception {
        pipeline.add(new Element("nothing"));
        reader.read(pipeline);
    }

}
