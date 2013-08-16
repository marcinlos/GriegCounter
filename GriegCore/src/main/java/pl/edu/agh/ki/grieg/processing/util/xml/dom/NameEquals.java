package pl.edu.agh.ki.grieg.processing.util.xml.dom;

import com.google.common.base.Predicate;

public class NameEquals implements Predicate<Node> {
    
    private final String name;
    
    public NameEquals(String name) {
        this.name = name;
    }
    
    @Override
    public boolean apply(Node e) {
        return name.equals(e.name());
    }
}
