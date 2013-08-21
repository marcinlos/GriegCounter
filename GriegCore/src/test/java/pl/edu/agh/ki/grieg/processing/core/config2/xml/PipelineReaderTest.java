package pl.edu.agh.ki.grieg.processing.core.config2.xml;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.agh.ki.grieg.processing.core.config.Context;
import pl.edu.agh.ki.grieg.processing.core.config2.XmlConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineAssemblerNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineElementNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineNode;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelinePartNode;
import pl.edu.agh.ki.grieg.util.xml.XmlException;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

@RunWith(MockitoJUnitRunner.class)
public class PipelineReaderTest extends ReaderTest {

    private PipelineReader reader;

    private PipelinePartNode[] nodes = {
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
    
    @Mock private Context ctx;

    @Before
    public void setup() throws XmlException {
        Reader<PipelinePartNode> elementReader = new PipelineElementReader();
        Reader<PipelinePartNode> assemblerReader = new PipelineAssemblerReader();
        reader = new PipelineReader(elementReader, assemblerReader);
    }

    @Test
    public void pipelineIsOk() throws Exception {
        PipelineNode node = new PipelineNode(nodes);
        assertEquals(node, reader.read(pipeline, ctx));
    }

    @Test(expected = XmlConfigException.class)
    public void failsWithInvalidData() throws Exception {
        pipeline.add(new Element("nothing"));
        reader.read(pipeline, ctx);
    }

}
