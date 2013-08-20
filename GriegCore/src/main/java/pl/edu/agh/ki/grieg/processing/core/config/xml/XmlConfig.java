package pl.edu.agh.ki.grieg.processing.core.config.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import pl.edu.agh.ki.grieg.processing.core.PipelineAssembler;
import pl.edu.agh.ki.grieg.processing.core.config.AssemblerDefinition;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.PropertiesDefinition;
import pl.edu.agh.ki.grieg.util.Properties;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "grieg")
public class XmlConfig implements PropertiesDefinition, AssemblerDefinition {

    @XmlElement
    private XmlPropertyList properties;

    @XmlElement
    private XmlPipeline pipeline;

    @Override
    public Properties buildProperties() throws ConfigException {
        return properties.buildProperties();
    }

    @Override
    public PipelineAssembler createAssembler() throws ConfigException {
        return pipeline.createAssembler();
    }

}
