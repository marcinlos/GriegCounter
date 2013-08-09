package pl.edu.agh.ki.grieg.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.chart.swing.ChannelsChart;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.playback.Player;
import pl.edu.agh.ki.grieg.processing.core.Bootstrap;
import pl.edu.agh.ki.grieg.processing.core.Processor;
import pl.edu.agh.ki.grieg.processing.core.ProcessorFactory;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.XmlFileSystemBootstrap;
import pl.edu.agh.ki.grieg.processing.model.AudioModel;

public class MainWindow extends JFrame {

    private static final Logger logger = LoggerFactory
            .getLogger(MainWindow.class);
    
    private static final int WIDTH = 450;
    
    private static final int HEIGHT = 500;
    

    private static final String CWDIR = "user.dir";
    
    private static final File CONFIG_FILE = new File("settings");

    private static final int BUFFER_SIZE = 8192;
    
    private final SettingsManager settingsManager;
    private final Settings settings;
    
    private final Player player = new Player(BUFFER_SIZE);
    private final ProcessorFactory processorFactory;
    
    private final ChannelsChart waveView;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu presetMenu;
    

    public MainWindow(String label) throws ConfigException {
        super(label);
        logger.info("Starting in directory {}", System.getProperty(CWDIR));
        settingsManager = new SettingsManager(CONFIG_FILE);
        settings = settingsManager.readSettings(); 
        
        // Bootstrap bootstrap = new DefaultAnalyzerBootstrap();
        Bootstrap bootstrap = new XmlFileSystemBootstrap("grieg-config.xml");
        processorFactory = bootstrap.createFactory();
        
        AudioModel model = new AudioModel();
        processorFactory.addListener(model);
        
        waveView = new ChannelsChart(model.getChartModel(), 1, 2);

        logger.info("Creating window");
        setupUI();

        this.addWindowListener(new ClosingListener());
    }

    private void processFile(File file) {
        String path = file.getAbsolutePath();
        logger.info("Opening file {}", path);
        try {
            final Processor proc = processorFactory.newFileProcessor(file);
            proc.openFile();
            
            enqueue(new PreAnalysis(proc));
            enqueue(new Analysis(proc));

            AudioFile audio = proc.getFile();
            logger.info("Playing...");
            player.play(audio);
        } catch (Exception e) {
            Dialogs.showError(this, e);
        }
    }
    
    private void enqueue(Runnable action) {
        executor.execute(action);
    }
    
    private Collection<String> getKnownExtensions() {
        return processorFactory.getFileLoader().getKnownExtensions();
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
        Collection<String> extensions = getKnownExtensions();
        logger.debug("Building file filter, found extensions: {}", extensions);
        String[] extArray = extensions.toArray(new String[0]);
        return new FileNameExtensionFilter("Audio", extArray);
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
                    processFile(file);
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
                processFile(file);
            }
        });
    }

    private void setupLayout() {
        add(waveView);
    }
    

    private final class PreAnalysis implements Runnable {
        private final Processor proc;

        PreAnalysis(Processor proc) {
            this.proc = proc;
        }

        @Override
        public void run() {
            logger.info("Gathering metadata");
            try {
                proc.preAnalyze();
                logger.info("Metadata gathered");
            } catch (AudioException | IOException e) {
                Dialogs.showError(MainWindow.this, e);
                logger.error("Error during preliminary analysis", e);
            }
        }
    }
    
    private final class Analysis implements Runnable {
        private final Processor proc;

        Analysis(Processor proc) {
            this.proc = proc;
        }

        @Override
        public void run() {
            try {
                logger.info("Beginning audio analysis");
                proc.analyze();
                logger.info("Audio analysis finished");
            } catch (AudioException | IOException e) {
                Dialogs.showError(MainWindow.this, e);
                logger.error("Error during the main analysis phase", e);
            }
        }
    }

    private class ClosingListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            logger.debug("Window closing");
        }
    }

}
