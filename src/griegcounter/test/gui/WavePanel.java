package griegcounter.test.gui;

import griegcounter.audio.Player;
import griegcounter.graphics.Drawable;
import griegcounter.graphics.Gfx;

import java.awt.Color;

public class WavePanel extends TwoChannelPannel {

    public WavePanel(Player player) {
        super(player);
        setBackground(Color.black);
        setupUI();
    }

    public void refreshPanels() {
        if (hasTrack()) {
            leftChannel.refresh();
            rightChannel.refresh();
            Drawable left = leftChannel.getDrawable();
            Drawable right = rightChannel.getDrawable();
            Gfx.drawWave(track, new Drawable[] { left,  right }, 10000);
        }
        drawPosition();
    }


}
