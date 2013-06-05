package griegcounter.test.gui;

import griegcounter.audio.Track;
import griegcounter.graphics.Drawable;
import griegcounter.graphics.Gfx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class PowerPanel extends JPanel {

    private Track track;
    private ImagePanel leftChannel;
    private ImagePanel rightChannel;

    public PowerPanel() {
        setBackground(Color.black);
        setupUI();
    }

    private void setupUI() {
        leftChannel = new ImagePanel();
        rightChannel = new ImagePanel();
        for (ImagePanel p: new ImagePanel[]{leftChannel, rightChannel}) {
            p.setBackground(Color.black);
            p.setMinimumSize(new Dimension(100, 50));
            p.setPreferredSize(new Dimension(400, 50));
            p.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
        }
        
        setupLayout();
    }
    
    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridheight = 1;
        c.gridy = 0;
        add(leftChannel, c);
        c.gridy += 1;
        c.weighty = 1.0;
        add(rightChannel, c);
    }

    public void refreshPanels() {
        if (hasTrack()) {
            leftChannel.refresh();
            rightChannel.refresh();
            Drawable left = leftChannel.getDrawable();
            Drawable right = rightChannel.getDrawable();
            Gfx.drawPower(track, new Drawable[] { left,  right }, 10000);
        }
    }

    public void setTrack(Track track) {
        this.track = track;
        refreshPanels();
        repaint();
    }

    public boolean hasTrack() {
        return track != null;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        refreshPanels();
    }

}
