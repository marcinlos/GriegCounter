package griegcounter.graphics;

public class PathDrawer {
    
    private Point pos;
    private Drawable canvas;

    public PathDrawer(Drawable canvas) {
        this.canvas = canvas;
    }
    
    public void put(Point p) {
        if (pos != null) {
            canvas.line(pos, p);
        }
        pos = p;
    }

}
