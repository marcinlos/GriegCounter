package griegcounter.graphics.transform;

public interface Transformable {

    void push(Transform t);
    Transform pop();
    void clear();
    
}
