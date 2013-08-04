package pl.edu.agh.ki.grieg.gfx;

import static com.google.common.base.Preconditions.checkArgument;
import pl.edu.agh.ki.grieg.util.Point;
import pl.edu.agh.ki.grieg.util.PointI;

public abstract class AbstractCanvas implements Canvas {

    /** Virtual width of the canvas */
    private float width;

    /** Virtual height of the canvas */
    private float height;

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
    public AbstractCanvas(float width, float height) {
        this.width = width;
        this.height = height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWidth() {
        return width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWidth(float width) {
        doSetWidth(width);
        refresh();
    }

    /**
     * Sets width without refreshing the content
     */
    private void doSetWidth(float width) {
        checkArgument(width > 0, "Width %s must be positive", width);
        this.width = width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setHeight(float height) {
        doSetHeight(height);
        refresh();
    }

    /**
     * Sets height without refreshing the content
     */
    private void doSetHeight(float height) {
        checkArgument(height > 0, "Height %s must be positive", height);
        this.height = height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSize(float width, float height) {
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
    protected PointI toScreen(float x, float y) {
        int sw = getScreenWidth();
        int sh = getScreenHeight();
        float invy = height - y;
        int screenx = (int) (x * sw / width);
        int screeny = (int) (invy * sh / height);
        return new PointI(screenx, screeny);
    }

}
