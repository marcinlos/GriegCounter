package pl.edu.agh.ki.grieg.util.xml.dom;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Class representing XML element.
 * 
 * @author los
 */
public class Element extends Node {

    /** List of direct children of this element */
    private final List<Element> children;

    /** Attribute map */
    private final Map<QName, Attribute> attributes;

    /**
     * Creates new empty {@link Element} with specified local name and no
     * namespace. Initial value is {@code null}.
     * 
     * @param name
     *            Local name of the element
     */
    public Element(String name) {
        this(null, name);
    }

    /**
     * Creates new empty {@link Element} with specified namespace and local
     * name. Initial value is {@code null}.
     * 
     * @param ns
     *            Namespace URI
     * @param name
     *            Local name
     */
    public Element(String ns, String name) {
        super(ns, name);
        this.children = Lists.newArrayList();
        this.attributes = Maps.newHashMap();
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Overriden to specialize the return type, using covariance of return types
     * to allow chained invocations with {@link Element} instead of {@link Node}.
     */
    @Override
    public Element val(String value) {
        super.val(value);
        return this;
    }

    /**
     * Appends new child element to this element. Must be non-{@code null}.
     * 
     * @param child
     *            Element to append as a child
     * @return {@code this} for chained invocations
     */
    public Element add(Element child) {
        children.add(checkNotNull(child));
        return this;
    }

    /**
     * @return Immutable list of children
     */
    public List<Element> children() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Returns {@link Iterable} containing child elements with specified local
     * name and the namespace of this element. Name must be non-{@code null}.
     * 
     * <p>
     * Note: the assymetry with analogous function for the attributes stems from
     * the fact that it is far more usual for the element to have the same
     * namespace as its parent, while attributes usually have no namespace. This
     * design decision leads to more intuitive code.
     * 
     * @param name
     *            Local name to filter children by
     * @return {@link Iterable} with specified elements
     * @see #child(String)
     */
    public Iterable<Element> children(String name) {
        return children(ns(), name);
    }

    /**
     * Returns {@link Iterable} containing child elements with specified
     * qualified name. Name must be non-{@code null}.
     * 
     * @param qname
     *            Fully qualified name to filter children by
     * @return {@link Iterable} with specified elements
     * @see #child(QName)
     */
    public Iterable<Element> children(QName qname) {
        return Iterables.filter(children, new QNameEquals(qname));
    }

    /**
     * Returns {@link Iterable} containing child elements with specified
     * namespace and local name. Namespace may be {@code null}, in which case
     * elements with no namespace will be searched for. Name must be non-
     * {@code null}.
     * 
     * @param ns
     *            Namespace URI
     * @param local
     *            Local name
     * @return {@link Iterable} with specified elements
     * @see #child(String, String)
     */
    public Iterable<Element> children(String ns, String local) {
        return children(new QName(ns, local));
    }

    /**
     * Retrieves the first child with specified local name and the namespace of
     * this element. If there is none, returns {@code null}. Name must be non-
     * {@code null}.
     * 
     * <p>
     * Note: the assymetry with analogous function for the attributes stems from
     * the fact that it is far more usual for the element to have the same
     * namespace as its parent, while attributes usually have no namespace. This
     * design decision leads to more intuitive code.
     * 
     * @param name
     *            Local name of the returned element
     * @return First matching element of {@code null}
     * @see #children(String)
     */
    public Element child(String name) {
        return child(ns(), name);
    }

    /**
     * Retrieves the first child with specified qualified name. If there is
     * none, returns {@code null}. Name must be non-{@code null}.
     * 
     * @param qname
     *            Qualified name of the returned element
     * @return First matching element of {@code null}
     * @see #children(QName)
     */
    public Element child(QName qname) {
        return Iterables.getFirst(children(qname), null);
    }

    /**
     * Retrieves the first child with specified namespace URI and local name. If
     * there is none, returns {@code null}. Namespace may be {@code null}, in
     * which case elements with no namespace will be searched for. Name must be
     * non-{@code null}.
     * 
     * @param ns
     *            Namespace URI
     * @param local
     *            Local name of the returned element
     * @return First matching element or {@code null}
     * @see #children(String, String)
     */
    public Element child(String ns, String local) {
        return child(new QName(ns, local));
    }

    /**
     * Adds new attribute to the element. If an attribute with the same fully
     * qualified name has already been present in the element, it is overriden.
     * Attribute must be non-{@code null}.
     * 
     * @param attr
     *            Attribute to add
     * @return {@code this} for chained invocations
     * @see #hasAttr(QName)
     * @see #hasAttr(String)
     */
    public Element add(Attribute attr) {
        attributes.put(attr.qname(), attr);
        return this;
    }

    /**
     * Checks whether the node has an attribute with specified local name and no
     * namespace. Name must be non-{@code null}.
     * 
     * @param name
     *            Local name
     * @return {@code true} if the node has the specified attribute,
     *         {@code false} otherwise
     */
    public boolean hasAttr(String name) {
        return hasAttr(new QName(null, name));
    }

    /**
     * Checks whether the node has an attribute with specified qualified name.
     * Name must be non-{@code null}.
     * 
     * @param name
     *            Qualified name
     * @return {@code true} if the node has the specified attribute,
     *         {@code false} otherwise
     */
    public boolean hasAttr(QName name) {
        return attributes.containsKey(name);
    }

    /**
     * @return Immutable map of attributes, with qualified names as keys and
     *         {@link Attribute} nodes as values
     */
    public Map<QName, Attribute> attrs() {
        return Collections.unmodifiableMap(attributes);
    }

    /**
     * Retrieves an attribute node with specified local name and no namespace.
     * Name must be non-{@code null}.
     * 
     * @param name
     *            Local name of the attribute
     * @return Specified attribute node or {@code null} if it does not exist
     */
    public Attribute attrNode(String name) {
        return attrNode(new QName(null, name));
    }

    /**
     * Retrieves an attribute node with specified qualified name. Name must be
     * non-{@code null}.
     * 
     * @param name
     *            Qualified name of the attribute
     * @return Specified attribute node or {@code null} if it does not exist
     */
    public Attribute attrNode(QName qname) {
        return attributes.get(qname);
    }

    /**
     * Retrieves the value of an attribute with specified local name and no
     * namespace, or {@code null} if there is no such attribute. Name must be
     * non-{@code null}.
     * 
     * @param name
     *            Local name of the attribute
     * @return Specified attribute node or {@code null} if it does not exist
     */
    public String attr(String name) {
        return attr(new QName(null, name));
    }

    /**
     * Retrieves the value of an attribute with specified qualified name, or
     * {@code null} if there is no such attribute. Name must be non-{@code null}
     * .
     * 
     * @param name
     *            Qualified name of the attribute
     * @return Specified attribute node or {@code null} if it does not exist
     */
    public String attr(QName qname) {
        Attribute attr = attrNode(qname);
        return attr == null ? null : attr.val();
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
