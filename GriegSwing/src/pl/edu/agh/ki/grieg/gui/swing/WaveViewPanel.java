package pl.edu.agh.ki.grieg.gui.swing;

import javax.swing.JPanel;

import pl.edu.agh.ki.grieg.gui.WavePresenter;
import pl.edu.agh.ki.grieg.gui.WaveView;
import pl.edu.agh.ki.grieg.processing.core.Analyzer;

public class WaveViewPanel extends JPanel implements WaveView {
    
    private final Analyzer analyzer;
    
    private final WavePresenter presenter;
    

    public WaveViewPanel(Analyzer analyzer) {
        this.analyzer = analyzer;
        this.presenter = new WavePresenter(this);
        analyzer.addListener(presenter);
    }

}
