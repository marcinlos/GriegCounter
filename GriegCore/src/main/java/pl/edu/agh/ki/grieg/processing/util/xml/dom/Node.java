package pl.edu.agh.ki.grieg.processing.util.xml.dom;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;


public abstract class Node {
    
    private final String ns;
    
    private final String name;
    
    private final QName qname;
    
    private String value;
    
    public Node(String ns, String name) {
        this.ns = ns;
        this.name = checkNotNull(name);
        this.qname = new QName(ns, name);
    }

    public String ns() {
        return ns;
    }

    public String name() {
        return name;
    }

    public QName qname() {
        return qname;
    }
    
    public Node val(String value) {
        this.value = value;
        return this;
    }

    public String val() {
        return value;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(ns, name, value);
    }
    
}
