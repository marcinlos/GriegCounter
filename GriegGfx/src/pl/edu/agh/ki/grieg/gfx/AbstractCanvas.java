package pl.edu.agh.ki.grieg.gfx;

import static com.google.common.base.Preconditions.checkArgument;
import pl.edu.agh.ki.grieg.util.math.Point;
import pl.edu.agh.ki.grieg.util.math.PointI;

public abstract class AbstractCanvas implements Canvas {

    /** Virtual width of the canvas */
    private double width;

    /** Virtual height of the canvas */
    private double height;

    /**
     * Creates new {@code 1 x 1} canvas.
     */
    public AbstractCanvas() {
        this(1, 1);
    }

    /**
     * Creates new {@code width x height} canvas.
     * 
     * @param width
     *            Width of the canvas
     * @param height
     *            Height of the canvas
     */
    public AbstractCanvas(double width, double height) {
        checkArgument(width > 0, "Width %f must be positive", width);
        checkArgument(width > 0, "Height %f must be positive", height);
        this.width = width;
        this.height = height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getWidth() {
        return width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWidth(double width) {
        doSetWidth(width);
        refresh();
    }

    /**
     * Sets width without refreshing the content
     */
    private void doSetWidth(double width) {
        checkArgument(width > 0, "Width %f must be positive", width);
        this.width = width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setHeight(double height) {
        doSetHeight(height);
        refresh();
    }

    /**
     * Sets height without refreshing the content
     */
    private void doSetHeight(double height) {
        checkArgument(height > 0, "Height %f must be positive", height);
        this.height = height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSize(double width, double height) {
        doSetWidth(width);
        doSetHeight(height);
        refresh();
    }

    /**
     * @return Width of the canvas in pixels in the actual screen space
     */
    protected abstract int getScreenWidth();

    /**
     * @return Height of the canvas in pixels in the actual screen space
     */
    protected abstract int getScreenHeight();

    /**
     * Transforms the point's virtual coordinates into the actual screen
     * coordinates, assuming standard coordinate system with vertical axis
     * pointing down.
     * 
     * @param p
     *            Point in virtual coordinates
     * @return Point in screen coordinates
     */
    protected PointI toScreen(Point p) {
        return toScreen(p.x, p.y);
    }

    /**
     * Transforms the virtual coordinates to the actual screen coordinates,
     * assuming standard coordinate system with vertical axis pointing down.
     * 
     * @param x
     *            Abcissa of the point
     * @param y
     *            Ordinate of the point
     * @return Point in screen coordinates
     */
    protected PointI toScreen(double x, double y) {
        int sw = getScreenWidth();
        int sh = getScreenHeight();
        double invy = height - y;
        int screenx = (int) (x * sw / width);
        int screeny = (int) (invy * sh / height);
        return new PointI(screenx, screeny);
    }

}
