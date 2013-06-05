package griegcounter.graphics;

import griegcounter.graphics.transform.Transformable;

public interface Drawable extends Transformable {

    float getWidth();
    float getHeight();
    
    void line(Point begin, Point end);
    
    void debugText(String text, Point pos);

}
