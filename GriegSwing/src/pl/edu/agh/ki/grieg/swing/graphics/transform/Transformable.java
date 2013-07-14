package pl.edu.agh.ki.grieg.swing.graphics.transform;

public interface Transformable {

    void push(Transform t);
    Transform pop();
    void clear();
    
}
