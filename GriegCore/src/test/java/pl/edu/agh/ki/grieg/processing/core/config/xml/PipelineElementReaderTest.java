package pl.edu.agh.ki.grieg.processing.core.config.xml;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.agh.ki.grieg.processing.core.config.Context;
import pl.edu.agh.ki.grieg.processing.core.config.evaluator.XmlConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PipelineElementNode;
import pl.edu.agh.ki.grieg.processing.core.config.xml.PipelineElementReader;
import pl.edu.agh.ki.grieg.util.xml.XmlException;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

@RunWith(MockitoJUnitRunner.class)
public class PipelineElementReaderTest extends ReaderTest {

    private Element first;
    private Element second;
    private Element third;

    private PipelineElementReader reader;

    private PipelineElementNode firstDefinition =
            new PipelineElementNode("segmenter",
                    "pl.edu.agh.ki.grieg.analysis.Segmenter", "ROOT");

    private PipelineElementNode secondDefinition =
            new PipelineElementNode("compressor",
                    "pl.edu.agh.ki.grieg.analysis.WaveCompressor", "segmenter");

    private PipelineElementNode thirdDefinition =
            new PipelineElementNode("hamming",
                    "pl.edu.agh.ki.grieg.analysis.HanningSegmenter", "ROOT");

    @Mock private Context context;

    @Before
    public void setup() throws XmlException {
        reader = new PipelineElementReader();
        first = pipeline.children().get(0);
        second = pipeline.children().get(1);
        third = pipeline.children().get(3);
    }
    
    @Test
    public void definitionsAreParsedCorrectly() throws XmlConfigException {
        PipelineElementNode firstActual = reader.read(first, context);
        PipelineElementNode secondActual = reader.read(second, context);
        PipelineElementNode thirdActual = reader.read(third, context);

        assertEquals("segmenter", firstActual.getName());
        assertEquals("pl.edu.agh.ki.grieg.analysis.WaveCompressor",
                secondActual.getType());
        assertEquals("ROOT", thirdActual.getSource());

        assertEquals(firstDefinition, firstActual);
        assertEquals(secondDefinition, secondActual);
        assertEquals(thirdDefinition, thirdActual);
    }

}
