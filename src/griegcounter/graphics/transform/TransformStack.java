package griegcounter.graphics.transform;

import griegcounter.graphics.Point;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class TransformStack implements Transform, Transformable {

    private Deque<Transform> stack = new ArrayDeque<>();

    public TransformStack() {
        // empty
    }

    public TransformStack(Transform transform) {
        stack.push(transform);
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

    @Override
    public Point apply(Point p) {
        for (Iterator<Transform> it = stack.descendingIterator(); 
                it.hasNext();) {
            p = it.next().apply(p);
        }
        return p;
    }

}
