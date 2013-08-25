package pl.edu.agh.ki.grieg.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.Controller;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.widgets.swing.ChannelsChart;
import pl.edu.agh.ki.grieg.widgets.swing.LabelView;
import pl.edu.agh.ki.grieg.widgets.swing.ProgressBar;
import pl.edu.agh.ki.grieg.widgets.swing.TitleBarPercentDisplay;

public class MainWindow extends JFrame {

    private static final Logger logger = LoggerFactory
            .getLogger(MainWindow.class);

    private static final int WIDTH = 450;
    private static final int HEIGHT = 500;

    private static final String CWDIR = "user.dir";
    private static final File CONFIG_FILE = new File("settings");

    private static final Class<? extends Set<String>> STRING_SET =
            Reflection.castClass(Set.class);

    private final SettingsManager settingsManager;
    private final Settings settings;

    private final Model<?> model;
    private final Controller controller;

    private final ChannelsChart waveView;
    private final ProgressBar progressBar;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu presetMenu;

	private LabelView labelView;

    public MainWindow(Model<?> model, Controller controller, String label) {
        super(label);
        this.model = model;
        this.controller = controller;

        logger.info("Starting in directory {}", System.getProperty(CWDIR));
        settingsManager = new SettingsManager(CONFIG_FILE);
        settings = settingsManager.readSettings();

        waveView = new ChannelsChart(model.getChild("wave"), 1, 2);
        
        labelView = new LabelView(model.getChild("dupee",String.class));
        

        model.getChild("preanalysis_progress", Float.class)
                .addListener(new TitleBarPercentDisplay(this));

        progressBar = new ProgressBar(model.getChild("preanalysis_progress",
                Float.class));

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
        Set<String> extensions = $("loader.extensions", STRING_SET);
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
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
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

    private void setupLayout() {
        add(waveView);
        add(progressBar.swingPanel(), BorderLayout.PAGE_END);
        add(labelView, BorderLayout.PAGE_START);
    }

    private class ClosingListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            logger.debug("Window closing");
        }
    }

}
