package pl.edu.agh.ki.grieg.processing.util.xml.dom;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class DomTest {

    private final static String NS = "http://some.random/xml/namespace";
    
    private Element element;
    
    @Before
    public void setup() {
        element = new Element(NS, "root");
    }
    
    @Test(expected = NullPointerException.class)
    public void cannotCreateWithNullName() {
        new Element(NS, null);
    }
    
    @Test
    public void canCreateWithNullNamespace() {
        new Element("sth");
    }
    
    @Test
    public void elementRemambersItsName() {
        assertEquals("root", element.name());
        assertEquals(NS, element.ns());
    }
    
    @Test
    public void elementHasCorrectQName() {
        assertEquals(new QName(NS, "root"), element.qname());
    }
    
    @Test
    public void defaultValueIsNull() {
        assertNull(element.val());
    }
    
    @Test
    public void canSetElementValue() {
        element.val("value");
        assertEquals("value", element.val());
    }
    
    @Test
    public void attributeEqualsAndHashWork() {
        Attribute a = new Attribute("class");
        Attribute b = new Attribute("class");
        Attribute c = new Attribute(NS, "name");
        
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(b, c);
        assertNotEquals(a, "class");
        assertNotEquals(c, c.qname());
        
        a.val("blue");
        b.val("red");
        
        assertNotEquals(a, b);
    }
    
    @Test
    public void canAddAttributes() {
        Attribute a = new Attribute("class");
        Attribute b = new Attribute(NS, "name");
        
        assertFalse(element.hasAttr("class"));
        assertFalse(element.hasAttr(new QName(NS, "name")));
        
        element.add(a);
        assertTrue(element.hasAttr("class"));
        assertFalse(element.hasAttr(new QName(NS, "name")));
        
        element.add(b);
        assertTrue(element.hasAttr("class"));
        assertTrue(element.hasAttr(new QName(NS, "name")));
    }
    
    @Test
    public void canGetAttributesByName() {
        Attribute a = new Attribute("class").val("one");
        Attribute b = new Attribute(NS, "name").val("two");
        element.add(a).add(b);

        assertEquals("one", element.attr("class"));
        assertEquals("two", element.attr(new QName(NS, "name")));
        assertNull(element.attr("there is no such attribute"));
        
        
        assertEquals(a, element.attrNode("class"));
        assertEquals(b, element.attrNode(new QName(NS, "name")));
    }
    
    @Test
    public void elementReturnsCorrectAttributeMap() {
        assertThat(element.attrs().keySet(), empty());
        
        Attribute a = new Attribute("class");
        Attribute b = new Attribute(NS, "name");
        element.add(a).add(b);
        
        Map<QName, Attribute> expected = ImmutableMap.<QName, Attribute>builder()
                .put(a.qname(), a)
                .put(b.qname(), b)
                .build();
        assertEquals(expected, element.attrs());
    }
    
    @Test
    public void canAddElements() {
        assertThat(element.children(), empty());

        Element b = new Element(NS, "tree");
        Element c = new Element(NS, "tree");
        Element d = new Element("bush");
        Element e = new Element(NS, "bush");
        
        element.add(b).add(c);
        assertEquals(Arrays.asList(b, c), element.children());
        
        element.add(d).add(e);
        assertEquals(Arrays.asList(b, c, d, e), element.children());
    }
    
    @Test
    public void canGetChildByName() {
        Element b = new Element(NS, "tree");
        Element c = new Element("bush");
        Element d = new Element(NS, "tree");
        Element e = new Element(NS, "bush");
        element.add(b).add(c).add(d).add(e);

        assertEquals(b, element.child(new QName(NS, "tree")));
        assertEquals(c, element.child("bush"));
        assertEquals(e, element.child(new QName(NS, "bush")));
    }
    
    @Test
    public void elementEqualsAndHashWork() {
        Attribute a = new Attribute("class");
        Attribute b = new Attribute(NS, "name");
        Element c = new Element("bush");
        Element d = new Element(NS, "tree");
        element.add(a).add(b).add(c).add(d);
        element.val("some value");

        Element otherNs = new Element("http://random.sth", "root");
        
        Element otherName = new Element(NS, "meh");
        
        Element otherVal = new Element(NS, "root");
        otherVal.val("bzzz");
        
        Element otherAttrs = new Element(NS, "root").add(b);
        otherAttrs.val("some value");
        
        Element otherChildren = new Element(NS, "root").add(b).add(a).add(c);
        otherChildren.val("some value");
        
        Element equal = new Element(NS, "root").add(a).add(b).add(c).add(d);
        equal.val("some value");
        
        assertNotEquals(element, "whatevah");
        assertNotEquals(element, otherNs);
        assertNotEquals(element, otherName);
        assertNotEquals(element, otherVal);
        assertNotEquals(element, otherAttrs);
        assertNotEquals(element, otherChildren);
        assertEquals(element, equal);
        assertEquals(element.hashCode(), equal.hashCode());
    }
    
    @Test
    public void canGetChildrenByName() {
        Element b = new Element(NS, "tree");
        Element c = new Element(NS, "tree");
        element.add(b).add(c);

        Iterable<Element> children = element.children(new QName(NS, "tree"));
        assertEquals(Arrays.asList(b, c), Lists.newArrayList(children));
    }
    
    @Test
    public void returnsNullForNonexistantChildren() {
        assertNull(element.child(new QName("heisenberg")));
        assertNull(element.child("rachmaninoff"));
    }

}
