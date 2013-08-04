package pl.edu.agh.ki.grieg.gui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.chart.swing.ChannelsChart;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.playback.Player;
import pl.edu.agh.ki.grieg.processing.core.ProcessorFactory;
import pl.edu.agh.ki.grieg.processing.core.Bootstrap;
import pl.edu.agh.ki.grieg.processing.core.Processor;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.XmlFileSystemBootstrap;
import pl.edu.agh.ki.grieg.processing.model.AudioModel;

import com.google.gson.Gson;

public class MainWindow extends JFrame {

    private static final Logger logger = LoggerFactory
            .getLogger(MainWindow.class);

    private static final String CWDIR = "user.dir";
    
    private static final File CONFIG_FILE = new File("settings");

    private static final int BUFFER_SIZE = 8192;
    
    private static final String[] EXTS = { "mp3", "wav" };
    

    private final Settings settings;
    
    private final Player player = new Player(BUFFER_SIZE);
    private final ProcessorFactory analyzer;
    
    private final ChannelsChart waveView;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu presetMenu;
    

    public MainWindow(String label) throws ConfigException {
        super(label);
        logger.info("Starting in directory {}", System.getProperty(CWDIR));
        settings = readSettings(); 
        
//        Bootstrap bootstrap = new DefaultAnalyzerBootstrap();
        Bootstrap bootstrap = new XmlFileSystemBootstrap("grieg-config.xml");
        analyzer = bootstrap.createFactory();
        
        AudioModel model = new AudioModel();
        analyzer.addListener(model);
        
        waveView = new ChannelsChart(model.getChartModel(), 1, 2);

        logger.info("Creating window");
        setupUI();

        this.addWindowListener(new ClosingListener());
    }

    private Settings readSettings() {
        Settings settings;
        String path = CONFIG_FILE.getAbsolutePath();
        logger.info("Attempting to read settings from {}", path);
        try (Reader config = new FileReader(CONFIG_FILE)) {
            Gson gson = new Gson();
            settings = gson.fromJson(config, Settings.class);
        } catch (IOException e) {
            logger.warn("Cannot read config file {}, reason:", path, e);
            logger.warn("Using default configuration");
            settings = createDefaultSettings();
        }
        logger.debug("Using following settings: ");
        logSettings(settings);
        return settings;
    }
    
    private void logSettings(Settings settings) {
        File dir = new File(settings.getDirectory());
        logger.debug("  dir = {}", dir.getAbsolutePath());
        logger.debug("  files = {");
        for (String file : settings.getFiles()) {
            logger.debug("    {}", file);
        }
        logger.debug("  }");
    }

    private Settings createDefaultSettings() {
        String path = System.getProperty(CWDIR);
        return new Settings(path);
    }

    private void openFile(File file) {
        String path = file.getAbsolutePath();
        logger.info("Opening file {}", path);
        try {
            final Processor proc = analyzer.newFileProcessor(file);
            proc.openFile();
            
            enqueue(new PreAnalysis(proc));
            enqueue(new Analysis(proc));

            AudioFile audio = proc.getFile();
            logger.info("Playing...");
            player.play(audio);
        } catch (Exception e) {
            displayErrorMessage(this, e);
        }
    }
    
    private void enqueue(Runnable action) {
        executor.execute(action);
    }

    private static void displayErrorMessage(Component parent, Throwable e) {
        logger.error("Error", e);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        writer.println("Application encountered an error.\n");
        writer.println("Reason:");
        writer.println(e);
        JOptionPane.showMessageDialog(parent, stringWriter, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private File chooseFile() {
        logger.debug("Opening file choosing dialog");
        JFileChooser chooser = new JFileChooser(settings.getDirectory());
        FileFilter filter = new FileNameExtensionFilter("Audio", EXTS);
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

    private void setupUI() {
        getContentPane().setBackground(Color.black);
        setupPosition();
        setupMenu();
        setupLayout();
    }

    private void setupPosition() {
        setLocationByPlatform(true);
        setPreferredSize(new Dimension(450, 500));
    }

    private void setupMenu() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.add(new AbstractAction("Open...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = chooseFile();
                if (file != null) {
                    openFile(file);
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
                openFile(file);
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
                displayErrorMessage(MainWindow.this, e);
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
                displayErrorMessage(MainWindow.this, e);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainWindow window = new MainWindow("GriegCounter");
                    window.pack();
                    window.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    window.setVisible(true);
                } catch (ConfigException e) {
                    displayErrorMessage(null, e);
                }
            }
        });
    }

}
