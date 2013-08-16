package pl.edu.agh.ki.grieg.processing.util.xml.dom;

import com.google.common.base.Objects;

public class Attribute extends Node {
    
    public Attribute(String name) {
        this(null, name);
    }

    public Attribute(String ns, String name) {
        super(ns, name);
    }

    public Attribute val(String value) {
        super.val(value);
        return this;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Attribute) {
            Attribute other = (Attribute) o;
            return Objects.equal(qname(), other.qname())
                    && Objects.equal(val(), other.val());
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", qname())
                .add("value", val())
                .toString();
    }
    
}
