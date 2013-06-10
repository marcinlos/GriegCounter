package griegcounter.graphics;

public class Point {
    
    public final float x;
    public final float y;
    
    public final static Point ORIGIN = new Point(0, 0);

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    Point move(float dx, float dy) {
        return new Point(x + dx, y + dy);
    }
    
    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }
}
