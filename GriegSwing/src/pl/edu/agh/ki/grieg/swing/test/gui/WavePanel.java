package pl.edu.agh.ki.grieg.swing.test.gui;

import java.awt.Color;

import pl.edu.agh.ki.grieg.swing.graphics.Drawable;

public class WavePanel extends TwoChannelPannel {

    public WavePanel(/*Player player*/) {
        super(/*player*/);
        setBackground(Color.black);
        setupUI();
    }

    public void refreshPanels() {
        if (/*hasTrack()*/true) {
            leftChannel.refresh();
            rightChannel.refresh();
            Drawable left = leftChannel.getDrawable();
            Drawable right = rightChannel.getDrawable();
//            Gfx.drawWave(track, new Drawable[] { left,  right }, 10000);
        }
        drawPosition();
    }


}
