package pl.edu.agh.ki.grieg.processing.core.config.xml;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.agh.ki.grieg.processing.core.config.Context;
import pl.edu.agh.ki.grieg.processing.core.config.tree.PipelineAssemblerNode;
import pl.edu.agh.ki.grieg.processing.core.config.xml.PipelineAssemblerReader;
import pl.edu.agh.ki.grieg.util.xml.XmlException;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

@RunWith(MockitoJUnitRunner.class)
public class PipelineAssemblerReaderTest extends ReaderTest {

    private static final String CLASS_NAME =
            "pl.edu.agh.ki.grieg.DefaultPipelineAssembler";

    private static final PipelineAssemblerNode assemblerNode = 
            new PipelineAssemblerNode(CLASS_NAME);

    private static PipelineAssemblerReader reader;
    
    @Mock private Context ctx;
    
    @BeforeClass
    public static void parseDocument() throws XmlException {
        reader = new PipelineAssemblerReader();
    }
    
    @Test
    public void assemblerIsParsedCorrectly() {
        Element assembler = root.child("pipeline").child("assembler");
        assertEquals(assemblerNode, reader.read(assembler, ctx));
    }

}
