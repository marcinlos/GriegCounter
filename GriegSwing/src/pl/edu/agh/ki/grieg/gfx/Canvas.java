package pl.edu.agh.ki.grieg.gfx;

/**
 * Interface of a generic paintable component. Has its own coordinate system,
 * with Y axis pointed up (unlike in most 2d graphic toolkits).
 * 
 * @author los
 */
public interface Canvas {

    /**
     * @return Width of the canvas in the virtual coordinate system
     */
    double getWidth();

    /**
     * Sets the width of the canvas in its virtual coordinate system. Causes
     * refresh of the contents.
     * 
     * @param width
     *            New width of the canvas
     */
    void setWidth(double width);

    /**
     * @return Height of the canvas in the virtual coordinate system. 
     */
    double getHeight();

    /**
     * Sets the height of the canvas in its virtual coordinate system. Causes
     * refresh of the contents.
     * 
     * @param height
     *            New height of the canvas
     */
    void setHeight(double height);

    /**
     * Sets the size of the canvas in its virtual coordinate system.Causes
     * refresh of the contents.
     * 
     * @param width
     *            New width of the canvas
     * @param height
     *            New height of the canvas
     */
    void setSize(double width, double height);

    /**
     * Forces repainting the canvas contents
     */
    void refresh();

}
