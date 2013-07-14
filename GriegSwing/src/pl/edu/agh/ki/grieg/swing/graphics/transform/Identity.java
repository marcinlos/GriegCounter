package pl.edu.agh.ki.grieg.swing.graphics.transform;

import pl.edu.agh.ki.grieg.swing.graphics.Point;

class Identity implements Transform {
    
    @Override
    public Point apply(Point p) {
        return p;
    }

}
