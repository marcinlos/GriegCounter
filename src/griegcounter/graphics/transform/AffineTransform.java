package griegcounter.graphics.transform;

import griegcounter.graphics.Point;

public class AffineTransform implements Transform {

    /*
     * x' = x0 + a * x + b * y y' = y0 + c * x + d * y
     */

    private float x0;
    private float y0;
    private float a;
    private float b;
    private float c;
    private float d;
    
    public AffineTransform() {
        a = 1;
        d = 1;
    }
    
    public AffineTransform(float x0, float y0, 
            float a, float b, float c, float d) {
        this.x0 = x0;
        this.y0 = y0;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Override
    public Point apply(Point p) {
        return new Point(
                x0 + a * p.x + b * p.y, 
                y0 + c * p.x + d * p.y
                );
    }
    
    public void move(float dx, float dy) {
        x0 += dx;
        y0 += dy;
    }
    
    
    public void scale(float sx, float sy) {
        x0 *= sx;
        a *= sx;
        b *= sx;
        y0 *= sy;
        c *= sy;
        d *= sy;
    }

}
