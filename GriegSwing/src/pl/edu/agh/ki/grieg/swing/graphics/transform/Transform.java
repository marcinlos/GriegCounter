package pl.edu.agh.ki.grieg.swing.graphics.transform;

import pl.edu.agh.ki.grieg.swing.graphics.Point;

public interface Transform {
    
    Point apply(Point p);

}
