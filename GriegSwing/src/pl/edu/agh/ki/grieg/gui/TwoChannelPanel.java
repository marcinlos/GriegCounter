package pl.edu.agh.ki.grieg.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.gui.swing.ImagePanel;

public abstract class TwoChannelPanel extends JPanel {
    
    private ImagePanel leftChannel;
    private ImagePanel rightChannel;

    protected void setupUI() {
        leftChannel = new ImagePanel();
        rightChannel = new ImagePanel();
        
        for (ImagePanel p: new ImagePanel[]{leftChannel, rightChannel}) {
            p.setBackground(Color.black);
//            p.setMinimumSize(new Dimension(100, 50));
//            p.setPreferredSize(new Dimension(400, 50));
            p.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
        }
        setupLayout();
    }
    
    protected ImagePanel left() {
        return leftChannel;
    }
    
    protected ImagePanel right() {
        return rightChannel;
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

}
