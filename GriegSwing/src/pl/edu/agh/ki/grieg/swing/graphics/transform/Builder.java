package pl.edu.agh.ki.grieg.swing.graphics.transform;

public interface Builder {
    
    Builder moveX(float dx);
    
    Builder moveY(float dy);
    
    Builder move(float dx, float dy);
    
    Builder scaleX(float a);
    
    Builder scaleY(float a);
    
    Builder scale(float sx, float sy);
    
    Builder flipV();
    
    Builder flipH();
    
    Transform build();

}
