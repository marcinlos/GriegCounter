package pl.edu.agh.ki.grieg.processing.util.xml.dom;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Predicate;

public class QNameEquals implements Predicate<Node> {
    
    private final QName name;
    
    public QNameEquals(QName name) {
        this.name = checkNotNull(name);
    }
    
    @Override
    public boolean apply(Node e) {
        return name.equals(e.qname());
    }
}
