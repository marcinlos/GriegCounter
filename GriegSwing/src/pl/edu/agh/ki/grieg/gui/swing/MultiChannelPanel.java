package pl.edu.agh.ki.grieg.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.google.common.collect.Lists;

public abstract class MultiChannelPanel extends JPanel {
    
    private final List<ImagePanel> channelPanels = Lists.newArrayList();
    
    protected void setupUI(int channels) {
        setBackground(Color.black);
        for (int i = 0; i < channels; ++ i) {
            ImagePanel panel = new ImagePanel();
            panel.setBackground(Color.black);
            panel.setPreferredSize(new Dimension(400, 50));
            panel.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
            channelPanels.add(panel);
        }
        setupLayout();
    }
    
    protected void refresh(int width, int height) {
        for (ImagePanel panel : channelPanels) {
            panel.refresh(width, height);
        }
    }
    
    protected void refresh() {
        for (ImagePanel panel : channelPanels) {
            panel.refresh();
        }
    }
    
    protected int channelCount() {
        return channelPanels.size();
    }
    
    protected List<ImagePanel> panels() {
        return channelPanels;
    }
    
    protected ImagePanel panel(int n) {
        return channelPanels.get(n);
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
        for (ImagePanel panel : channelPanels) {
            add(panel, c);
            ++ c.gridy;
        }
    }

}
