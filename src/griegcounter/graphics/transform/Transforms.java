package griegcounter.graphics.transform;

public class Transforms {

    private Transforms() {
        // non-instantiable
    }
    
    public static Transform IDENTITY = new Identity();
    
    public static Transform move(float dx, float dy) {
        return new AffineTransform(dx, dy, 0, 0, 0, 0);
    }

    public static Transform scale(float sx, float sy) {
        return new AffineTransform(0, 0, sx, 0, sy, 0);
    }
    
    public static Transform flipV() {
        return new AffineTransform(0, 0, 1, 0, -1, 0);
    }
    
    public static Transform flipH() {
        return new AffineTransform(0, 0, -1, 0, 1, 0);
    }
    
    public static Builder newBuilder() {
        return new ConcreteTransformBuilder();
    }
    
    
}
