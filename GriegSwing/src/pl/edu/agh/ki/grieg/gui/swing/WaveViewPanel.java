package pl.edu.agh.ki.grieg.gui.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.gui.WavePresenter;
import pl.edu.agh.ki.grieg.gui.WaveView;
import pl.edu.agh.ki.grieg.processing.core.Analyzer;
import pl.edu.agh.ki.grieg.swing.graphics.Drawable;
import pl.edu.agh.ki.grieg.swing.graphics.Drawables;
import pl.edu.agh.ki.grieg.swing.graphics.Point;
import pl.edu.agh.ki.grieg.utils.Range;

public class WaveViewPanel extends MultiChannelPanel implements WaveView {

    private static final Logger logger = LoggerFactory.getLogger(WaveViewPanel.class);

    private final WavePresenter presenter;

    public WaveViewPanel(Analyzer analyzer, int maxChannels) {
        logger.debug("Panel created");
        this.presenter = new WavePresenter(this);
        analyzer.addListener(presenter);

        setupUI(maxChannels);
        refresh(1000, 50);
    }

    @Override
    public void drawRange(float x, Range[] ranges) {
        int channels = Math.min(channelCount(), ranges.length);
        logger.trace("Drawing wave: x={}, {} channels", x, channels);
        for (int i = 0; i < channels; ++i) {
            Drawable d = panel(i).getDrawable();
            Drawables.setBounds(d, 0, 1, -1, 1);
            d.line(new Point(x, ranges[i].min), new Point(x, ranges[i].max));
        }
    }

}
