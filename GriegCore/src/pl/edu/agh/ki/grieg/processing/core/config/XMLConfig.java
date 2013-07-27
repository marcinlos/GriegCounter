package pl.edu.agh.ki.grieg.processing.core.config;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;

@XmlRootElement(name = "grieg")
class XMLConfig {
    
    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    public List<XMLProperty> properties = Lists.newArrayList();
    

}
