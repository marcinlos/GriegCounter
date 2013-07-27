package pl.edu.agh.ki.grieg.gui;

import pl.edu.agh.ki.grieg.util.Range;

public interface WaveView {
    
    void repaint();
    
    void drawRange(float x, Range[] ranges);

}
