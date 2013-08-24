package pl.edu.agh.ki.grieg.widgets.swing;

import java.awt.Frame;

import javax.swing.SwingUtilities;

import pl.edu.agh.ki.grieg.model.Listener;

public class TitleBarPercentDisplay implements Listener<Float> {

    private final Frame window;

    public TitleBarPercentDisplay(Frame window) {
        this.window = window;
    }

    @Override
    public void update(final Float percent) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                window.setTitle(formatPercent(percent));
            }
        });
    }

    private static final String formatPercent(float percent) {
        return String.format("%5.2f%%", 100 * percent);
    }
}