package pl.edu.agh.ki.grieg.swing.graphics.transform;

class ConcreteTransformBuilder implements Builder {
    
    private AffineTransform transform = new AffineTransform();

    @Override
    public Builder moveX(float dx) {
        return move(dx, 0);
    }

    @Override
    public Builder moveY(float dy) {
        return move(0, dy);
    }

    @Override
    public Builder move(float dx, float dy) {
        transform.move(dx, dy);
        return this;
    }

    @Override
    public Builder scaleX(float a) {
        return scale(a, 1);
    }

    @Override
    public Builder scaleY(float a) {
        return scale(1, a);
    }

    @Override
    public Builder scale(float sx, float sy) {
        transform.scale(sx, sy);
        return this;
    }

    @Override
    public Builder flipV() {
        return scaleY(-1);
    }

    @Override
    public Builder flipH() {
        return scaleX(-1);
    }

    @Override
    public Transform build() {
        return transform;
    }

}
