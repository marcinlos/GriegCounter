package pl.edu.agh.ki.grieg.util.xml.dom;

import static org.junit.Assert.*;

import org.junit.Test;

import pl.edu.agh.ki.grieg.util.xml.dom.QName;

public class NameTest {
    
    private static final String NS = "http://some.namespace";

    private QName normal = new QName(NS, "node-name");
    
    private QName nullNamespace = new QName("node-name");

    @Test(expected = NullPointerException.class)
    public void cannotCreateNameWithNullLocalComponent() {
        new QName(NS, null);
    }
    
    @Test
    public void toStringWithNonNullNamespaceWorks() {
        assertEquals(NS + ":node-name", normal.toString());
    }
    
    @Test
    public void toStringWithNullNamespaceWorks() {
        assertEquals("node-name", nullNamespace.toString());
    }
    
    @Test
    public void hashAndEqualsWork() {
        QName a = new QName(NS, "node");
        QName b = new QName(NS, "node");
        QName c = new QName(NS, "other");
        QName d = new QName("basdf", "other");
        
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        
        assertNotEquals(a, c);
        assertNotEquals(b, c);
        assertNotEquals(c, d);
        assertNotEquals(a, d);
        assertNotEquals(b, d);
        
        assertNotEquals(a, NS + ":node");
    }

}
