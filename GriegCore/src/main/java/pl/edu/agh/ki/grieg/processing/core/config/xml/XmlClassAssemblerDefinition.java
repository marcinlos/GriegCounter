package pl.edu.agh.ki.grieg.processing.core.config.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import pl.edu.agh.ki.grieg.processing.core.config.ClassAssemblerDefinition;

@XmlType
public class XmlClassAssemblerDefinition extends ClassAssemblerDefinition {
    
    private static final Object[] EMPTY = {};

    @XmlAttribute(name = "class")
    private String className;
    
    @Override
    protected String className() {
        return className;
    }

    @Override
    protected Object[] arguments() {
        return EMPTY;
    }

}
