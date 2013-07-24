package pl.edu.agh.ki.grieg.swing.test.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public abstract class TwoChannelPannel extends JPanel {

    protected ImagePanel leftChannel;
    protected ImagePanel rightChannel;
    
    private float start;
    private float position;


    protected void setupUI() {
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

    protected abstract void refreshPanels();

    protected void drawPosition() {
        drawPosition(position - start);
    }
    
    protected void drawPosition(float pos) {
        /*if (hasTrack()) {
            float total = track.getSample().getLength();
            for (ImagePanel p: new ImagePanel[] { leftChannel, rightChannel} ) {
                Drawable d = p.getDrawable();
                Gfx.setBounds(d, 0, total, 0, 1);
                d.line(new Point(pos, 0), new Point(pos, 1));
            }
        }*/
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        refreshPanels();
    }

}