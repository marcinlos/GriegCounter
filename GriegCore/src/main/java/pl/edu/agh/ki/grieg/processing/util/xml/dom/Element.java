package pl.edu.agh.ki.grieg.processing.util.xml.dom;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Element extends Node {

    private final List<Element> children;

    private final Map<QName, Attribute> attributes;

    public Element(String name) {
        this(null, name);
    }

    public Element(String ns, String name) {
        super(ns, name);
        this.children = Lists.newArrayList();
        this.attributes = Maps.newHashMap();
    }
    
    @Override
    public Element val(String value) {
        super.val(value);
        return this;
    }

    public Element add(Element child) {
        children.add(child);
        return this;
    }

    public List<Element> children() {
        return Collections.unmodifiableList(children);
    }

    public Iterable<Element> children(String name) {
        return Iterables.filter(children, new NameEquals(name));
    }

    public Iterable<Element> children(QName qname) {
        return Iterables.filter(children, new QNameEquals(qname));
    }

    public Element child(String name) {
        return Iterables.getFirst(children(name), null);
    }

    public Element child(QName qname) {
        return Iterables.getFirst(children(qname), null);
    }

    public Element add(Attribute attr) {
        attributes.put(attr.qname(), attr);
        return this;
    }

    public boolean hasAttr(String name) {
        return hasAttr(new QName(null, name));
    }

    public boolean hasAttr(QName name) {
        return attributes.containsKey(name);
    }

    public Map<QName, Attribute> attrs() {
        return Collections.unmodifiableMap(attributes);
    }

    public Attribute attr(String name) {
        return attr(new QName(null, name));
    }

    public Attribute attr(QName qname) {
        return attributes.get(qname);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Element) {
            Element other = (Element) o;
            return Objects.equal(qname(), other.qname())
                    && Objects.equal(val(), other.val())
                    && attributes.equals(other.attributes)
                    && children.equals(other.children);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), children, attributes);
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", qname())
                .add("value", val())
                .add("attrs", attributes)
                .add("children", children)
                .toString();
    }

}
