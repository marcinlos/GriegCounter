package pl.edu.agh.ki.grieg.util.math;

public class PointI {

    public final int x;

    public final int y;


    public PointI(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

}
