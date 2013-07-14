package pl.edu.agh.ki.grieg.swing.test.gui;

import java.awt.Color;

import pl.edu.agh.ki.grieg.swing.graphics.Drawable;


public class PowerPanel extends TwoChannelPannel {

    public PowerPanel(/*Player player*/) {
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
            //Gfx.drawPower(track, new Drawable[] { left,  right }, 10000);
        }
        drawPosition();
    }

}
