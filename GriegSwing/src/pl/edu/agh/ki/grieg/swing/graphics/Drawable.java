package pl.edu.agh.ki.grieg.swing.graphics;

import pl.edu.agh.ki.grieg.swing.graphics.transform.Transformable;

public interface Drawable extends Transformable {

    float getWidth();
    float getHeight();
    
    void line(Point begin, Point end);
    
    void debugText(String text, Point pos);

}
