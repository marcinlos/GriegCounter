package pl.edu.agh.ki.grieg.gfx;

public final class Point {
    
    public static final Point ORIGIN = new Point(0, 0);
    
    public final float x;
    
    public final float y;

    
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public float normSq() {
        return x * x + y * y;
    }
    
    public float norm() {
        return (float) Math.sqrt(normSq());
    }

    
    
    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

}
