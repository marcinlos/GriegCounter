package pl.edu.agh.ki.grieg.processing.core.config2.xml;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.agh.ki.grieg.processing.core.config.Context;
import pl.edu.agh.ki.grieg.processing.core.config.xml.XmlConfigException;
import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineElementNode;
import pl.edu.agh.ki.grieg.util.xml.XmlException;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

@RunWith(MockitoJUnitRunner.class)
public class XmlPipelineElementReaderTest extends XmlReaderTest {

    private Element first;
    private Element second;
    private Element third;

    private XmlPipelineElementReader reader;

    private PipelineElementNode firstDefinition =
            new PipelineElementNode("segmenter",
                    "pl.edu.agh.ki.grieg.analysis.Segmenter", "ROOT");
    
    private PipelineElementNode secondDefinition =
            new PipelineElementNode("compressor",
                    "pl.edu.agh.ki.grieg.analysis.WaveCompressor", "segmenter");
    
    private PipelineElementNode thirdDefinition =
            new PipelineElementNode("hamming",
                    "pl.edu.agh.ki.grieg.analysis.HammingSegmenter", "ROOT");
    
    @Mock private Context ctx;

    @Before
    public void setup() throws XmlException {
        reader = new XmlPipelineElementReader();
        first = pipeline.children().get(0);
        second = pipeline.children().get(1);
        third = pipeline.children().get(3);
    }

    @Test
    public void definitionsAreParsedCorrectly() throws XmlConfigException {
        assertEquals(firstDefinition, reader.read(first, ctx));
        assertEquals(secondDefinition, reader.read(second, ctx));
        assertEquals(thirdDefinition, reader.read(third, ctx));
    }


}
