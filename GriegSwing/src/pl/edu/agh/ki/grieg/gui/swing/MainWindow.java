package pl.edu.agh.ki.grieg.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.Controller;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.widgets.swing.ChannelsChart;
import pl.edu.agh.ki.grieg.widgets.swing.LogSpectrumPanel;
import pl.edu.agh.ki.grieg.widgets.swing.ProgressBar;
import pl.edu.agh.ki.grieg.widgets.swing.SpectrogramPanel;
import pl.edu.agh.ki.grieg.widgets.swing.SpectrumBars;
import pl.edu.agh.ki.grieg.widgets.swing.SpectrumPanel;
import pl.edu.agh.ki.grieg.widgets.swing.TitleBarPercentDisplay;

public class MainWindow extends JFrame {

    private static final Logger logger = LoggerFactory
            .getLogger(MainWindow.class);

    private static final String CWDIR = "user.dir";
    private static final File CONFIG_FILE = new File("settings");

    private static final Class<? extends Set<String>> STRING_SET =
            Reflection.castClass(Set.class);

    private final SettingsManager settingsManager;
    private final Settings settings;

    private final Model<?> model;
    private final Controller controller;

    private final ChannelsChart waveView;
    private final ChannelsChart waveWindowWideView;
    private final ChannelsChart waveWindowNarrowView;
    private final ChannelsChart powerChart;
    private final SpectrumPanel spectrumPanel;
    private final LogSpectrumPanel logSpectrumPanel;
    private final SpectrumBars spectrumBars;
    private final SpectrogramPanel spectrogramPanel;
    private final ProgressBar progressBar;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu presetMenu;

    public MainWindow(Model<?> model, Controller controller, String label) {
        super(label);
        this.model = model;
        this.controller = controller;

        logger.info("Starting in directory {}", System.getProperty(CWDIR));
        settingsManager = new SettingsManager(CONFIG_FILE);
        settings = settingsManager.readSettings();

        waveView = new ChannelsChart("Wave", model.getChild("wave.amplitude"), 1, -1, 1);

        waveWindowNarrowView = new ChannelsChart("Window (narrow)", 
                model.getChild("wave.window.narrow"), 1, -1, 1);
        waveWindowWideView = new ChannelsChart("Window (wide)", 
                model.getChild("wave.window.wide"), 1, -1, 1);

        powerChart = new ChannelsChart("Power", model.getChild("wave.power"), 1, -0.2, 1);

        Model<float[]> powerSpectrum = model.getChild("wave.fft.power", float[].class);
        spectrumPanel = new SpectrumPanel(powerSpectrum);
        logSpectrumPanel = new LogSpectrumPanel(powerSpectrum);
        spectrumBars = new SpectrumBars(powerSpectrum);
        spectrogramPanel = new SpectrogramPanel();

        Model<Float> progressModel = model.getChild("preanalysis.progress", Float.class); 
        
        progressModel.addListener(new TitleBarPercentDisplay(this));
        progressBar = new ProgressBar(progressModel);

        logger.info("Creating window");
        setupUI();

        this.addWindowListener(new ClosingListener());
    }

    private File chooseFile() {
        logger.debug("Opening file choosing dialog");
        JFileChooser chooser = new JFileChooser(settings.getDirectory());
        FileFilter filter = buildAudioFileFilter();
        chooser.setFileFilter(filter);
        int ret = chooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File choosen = chooser.getSelectedFile();
            logger.debug("Choosen file: {}", choosen.getAbsolutePath());
            return choosen;
        } else {
            logger.debug("File choosing cancelled");
            return null;
        }
    }

    private FileFilter buildAudioFileFilter() {
        Set<String> extensions = $("sys.loader.extensions", STRING_SET);
        logger.debug("Building file filter, found extensions: {}", extensions);
        String[] extArray = extensions.toArray(new String[0]);
        return new FileNameExtensionFilter("Audio", extArray);
    }

    private <T> T $(String path, Class<T> type) {
        return model.getChild(path, type).getData();
    }

    private void setupUI() {
        getContentPane().setBackground(Color.black);
        setupPosition();
        setupMenu();
        setupLayout();
    }

    private void setupPosition() {
        setLocationByPlatform(true);
    }

    private void setupMenu() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.add(new AbstractAction("Open...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = chooseFile();
                if (file != null) {
                    controller.processFile(file);
                }
            }
        });
        presetMenu = new JMenu("Recent");
        for (String path : settings.getFiles()) {
            addPreset(presetMenu, new File(path));
        }

        menuBar.add(fileMenu);
        menuBar.add(presetMenu);
        setJMenuBar(menuBar);
    }

    private void addPreset(JMenu menu, final File file) {
        menu.add(new AbstractAction(file.getName()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.processFile(file);
            }
        });
    }
    
    private Component withSize(Component c, Dimension min, Dimension pref) {
        c.setMinimumSize(min);
        c.setPreferredSize(pref);
        return c;
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        
        JPanel innerPanel = new JPanel();
        mainPanel.add(innerPanel);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.LINE_AXIS));
        
        JPanel charts = new JPanel();
        charts.setLayout(new BoxLayout(charts, BoxLayout.PAGE_AXIS));
        
        Dimension min = new Dimension(300, 100);
        Dimension pref = new Dimension(500, 130);

        charts.add(withSize(waveView, min, pref));
        charts.add(withSize(powerChart, min, pref));
        charts.add(withSize(waveWindowWideView, min, pref));
        charts.add(withSize(waveWindowNarrowView, min, pref));

        min = new Dimension(300, 120);
        pref = new Dimension(450, 120);
        
        JPanel spectra = new JPanel();
        spectra.setLayout(new BoxLayout(spectra, BoxLayout.PAGE_AXIS));
        
        min = new Dimension(300, 120);
        pref = new Dimension(450, 120);
        
        spectra.add(withSize(spectrumPanel, min, pref));
        spectra.add(withSize(logSpectrumPanel, min, pref));
        spectra.add(withSize(spectrumBars, min, pref));

        innerPanel.add(charts);
        innerPanel.add(spectra);

        min = new Dimension(400, 200);
        pref = new Dimension(500, 200);
        mainPanel.add(withSize(spectrogramPanel, min, pref));
        
        add(mainPanel);
        add(progressBar, BorderLayout.PAGE_END);
    }

    private class ClosingListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            logger.debug("Window closing");
        }
    }

}
