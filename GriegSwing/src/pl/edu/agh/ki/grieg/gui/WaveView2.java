package pl.edu.agh.ki.grieg.gui;

import pl.edu.agh.ki.grieg.swing.graphics.Point;
import pl.edu.agh.ki.grieg.utils.Range;

public interface WaveView2 extends WaveView {
    
    void drawLine(int channel, Point begin, Point end);

}
