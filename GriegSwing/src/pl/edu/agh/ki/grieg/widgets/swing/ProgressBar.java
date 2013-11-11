package pl.edu.agh.ki.grieg.widgets.swing;

import static com.google.common.base.Preconditions.checkNotNull;
import pl.edu.agh.ki.grieg.model.Listener;
import pl.edu.agh.ki.grieg.model.Model;

public class ProgressBar extends ProgressBarView implements Listener<Float> {

    public ProgressBar(Model<Float> serie) {
        checkNotNull(serie);
        serie.addListener(this);
        setData(serie.getData());
        repaint();
    }

    @Override
    public void update(Float data) {
        setData(data);
    }

}
