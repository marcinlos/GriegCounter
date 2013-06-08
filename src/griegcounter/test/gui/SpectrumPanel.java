package griegcounter.test.gui;

import static griegcounter.util.Utils.clamp;
import static griegcounter.util.Utils.db;
import griegcounter.audio.Player;
import griegcounter.graphics.Drawable;
import griegcounter.graphics.Gfx;
import griegcounter.graphics.Point;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import net.beadsproject.beads.analysis.FeatureExtractor;
import net.beadsproject.beads.analysis.featureextractors.PowerSpectrum;
import net.beadsproject.beads.core.TimeStamp;

public class SpectrumPanel extends JPanel {
    
    private Player player;
    private ImagePanel panel;
    private PowerSpectrum spectrum;

    private class PowerListener extends FeatureExtractor<Void, float[]> {
        
        private int c = 0;
        
        @Override
        public void process(TimeStamp a, TimeStamp b, float[] data) {
            //System.out.println("Size: " + data.length);
            //System.out.println(Arrays.toString(data));
            //System.out.println(data[1000]);
//            if (++c % 10 == 0) {
                panel.refresh();
                paintSpectrum(data);
                repaint();
//            }
        }
    }
    
    public SpectrumPanel(Player player, PowerSpectrum spectrum) {
        this.player = player;
        this.spectrum = spectrum;
        setBackground(Color.black);
        spectrum.addListener(new PowerListener());
        setupUI();
    }
    
    private void setupUI() {
        setLayout(new BorderLayout());
        panel = new ImagePanel();
        panel.setMinimumSize(new Dimension(200, 100));
        panel.setPreferredSize(new Dimension(300, 200));
        panel.setBackground(Color.black);
        panel.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
        add(panel);
    }
    
    private static final float MIN = -100;
    private static final float MAX = 60;

    private static final float SCALE_WIDTH = 1000.0f;
    
    private float getBinSize() {
        return player.getFormat().getSampleRate() / player.getChunkSize();
    }
    
    private float getNyquist() {
        return player.getFormat().getSampleRate() / 2;
    }
    
    private void paintSpectrum(float[] data) {
        Drawable d = panel.getDrawable();
        int N = data.length;
        Gfx.setBounds(d, 0, N - 1, MIN, MAX);
        
        Graphics2D g = ((DrawableGraphics2d) d).getGraphics();
        Color c = g.getColor();
        g.setColor(Color.gray);

        float unit = getBinSize();
        for (int i = 1; i < getNyquist() / SCALE_WIDTH; ++ i) {
            float x = i * SCALE_WIDTH / unit;
            d.line(new Point(x, MIN), new Point(x, MAX));
        }
        g.setColor(c);
        
        for (int i = 1; i < N; ++ i) {
            float val = clamp(db(data[i] / N), MIN, MAX);
            d.line(new Point(i, MIN), new Point(i, val));
        }
        //d.line(new Point(0, 0), new Point(N - 1, 0));
    }
    
    private void paintSpectrumLog(float[] data) {
        Drawable d = panel.getDrawable();
        Gfx.setBounds(d, 0, (float) Math.log(data.length), MIN, MAX);
        for (int i = 1; i < data.length; ++ i) {
            float val = clamp(db(data[i]), MIN, MAX);
            float x = (float) Math.log(i);
            d.line(new Point(x, MIN), new Point(x, val));
            //if (i % 2 == 0)
            //d.debugText(String.valueOf(i), new Point(x, MAX - 40));
        }
    }
    
    public void refresh() {
        panel.refresh();
        repaint();
    }

}
