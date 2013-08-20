package pl.edu.agh.ki.grieg.processing.core.config.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

import pl.edu.agh.ki.grieg.processing.core.PipelineAssembler;
import pl.edu.agh.ki.grieg.processing.core.config.AssemblerDefinition;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;

@XmlType
public class XmlPipeline implements AssemblerDefinition {

    @XmlElements({
        @XmlElement(name = "assembler",
                type = XmlClassAssemblerDefinition.class)
    })
    private AssemblerDefinition assembler;

    @Override
    public PipelineAssembler createAssembler() throws ConfigException {
        return assembler.createAssembler();
    }

}
