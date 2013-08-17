package pl.edu.agh.ki.grieg.processing.core.config2.xml;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import pl.edu.agh.ki.grieg.processing.core.config2.tree.PipelineAssemblerNode;
import pl.edu.agh.ki.grieg.util.xml.XmlException;
import pl.edu.agh.ki.grieg.util.xml.dom.Element;

public class XmlPipelineAssemblerReaderTest extends XmlReaderTest {

    private static final String CLASS_NAME =
            "pl.edu.agh.ki.grieg.DefaultPipelineAssembler";

    private static final PipelineAssemblerNode assemblerNode = 
            new PipelineAssemblerNode(CLASS_NAME);

    private static XmlPipelineAssemblerReader reader;
    
    @BeforeClass
    public static void parseDocument() throws XmlException {
        reader = new XmlPipelineAssemblerReader();
    }
    
    @Test
    public void assemblerIsParsedCorrectly() {
        Element assembler = root.child("pipeline").child("assembler");
        assertEquals(assemblerNode, reader.read(assembler));
    }

}
