package pl.edu.agh.ki.grieg.processing.core.config.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@XmlRootElement(name = "grieg")
public class XmlConfig {
    
//    @XmlElementWrapper
//    @XmlElements({
//        @XmlElement(name = "string", type = XmlStringProperty.class),
//        @XmlElement(name = "int", type = XmlIntProperty.class),
//        @XmlElement(name = "long", type = XmlLongProperty.class),
//    })
//    public List<XmlProperty<?>> properties = Lists.newArrayList();
    
    @XmlElement(name = "properties")
    public XmlPropertyList properties;

}
