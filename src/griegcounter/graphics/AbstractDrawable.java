package griegcounter.graphics;

import griegcounter.graphics.transform.Transform;
import griegcounter.graphics.transform.TransformStack;

public abstract class AbstractDrawable implements Drawable {

    private TransformStack stack = new TransformStack();
    
    protected TransformStack getTransformStack() {
        return stack;
    }
    
    protected Point transform(Point p) {
        return stack.apply(p);
    }

    @Override
    public void push(Transform t) {
        stack.push(t);
    }

    @Override
    public Transform pop() {
        return stack.pop();
    }

    @Override
    public void clear() {
        stack.clear();
    }

}
