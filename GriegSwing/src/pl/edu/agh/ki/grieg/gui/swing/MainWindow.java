package pl.edu.agh.ki.grieg.gui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.AbstractAction;
import javax.swing.Box;
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

import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.playback.Player;
import pl.edu.agh.ki.grieg.processing.core.Analyzer;
import pl.edu.agh.ki.grieg.processing.core.Bootstrap;
import pl.edu.agh.ki.grieg.processing.core.Processor;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.XmlFileSystemBootstrap;

public class MainWindow extends JFrame {

    private static final Logger logger = LoggerFactory
            .getLogger(MainWindow.class);

    private static final int BUFFER_SIZE = 8192;

    private final Player player = new Player(BUFFER_SIZE);
    private final Analyzer analyzer;
    

    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu presetMenu;

    private WaveViewPanel waveView;
    // private TwoChannelPannel powerPanel;
    // private SpectrumPanel spectrumPanel;

    private static final String DIR = "/media/los/Data/muzyka/Klasyczna/";
    private static final String FILE = DIR + "Bach/Inventions/Invention no. 8 (F major).mp3";
    private static final String BEETH = DIR + "Beethoven/Op. 109 (PS no. 30 in E major)/Piano Sonata no.30 in E major op.109 - II- Prestissimo.mp3";
    private static final String BIG = DIR + "Beethoven/Beethoven's 9th.mp3";
    private static final String RACH = DIR + "Rachmaninov/Op. 28 (PS no. 1 in D minor)/03 Piano Sonata No.1 in D minor Op.28 - III. Allegro molto.mp3";

    private static final String[] EXTS = { "mp3", "wav" };

    public MainWindow(String label) throws ConfigException {
        super(label);
        
        Bootstrap bootstrap = new XmlFileSystemBootstrap("grieg-config.xml");
//        Bootstrap bootstrap = new DefaultAnalyzerBootstrap();
        analyzer = bootstrap.createAnalyzer();
        
        logger.info("Creating window");
        getContentPane().setBackground(Color.black);

        waveView = new WaveViewPanel(analyzer, 2);

        this.addWindowListener(new ClosingListener());
        setupUI();

        /*
         * FFT fft = new FFT(); player.addAnalysis(fft); PowerSpectrum ps = new
         * PowerSpectrum(); fft.addListener(ps); SpectralDifference sd = new
         * SpectralDifference(ac.getSampleRate()); ps.addListener(sd);
         * PeakDetector beatDetector = new PeakDetector();
         * sd.addListener(beatDetector); beatDetector.setThreshold(0.2f);
         * beatDetector.setAlpha(.3f);
         */
    }

    private void openFile(File file) {
        try {
            logger.info("Opening file {}", file);
            final Processor proc = analyzer.newProcessing(file);
            proc.openFile();
            logger.info("Gathering metadata");
            proc.preAnalyze();
            logger.info("Metadata gathered");

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info("Beginning audio analysis");
                        proc.analyze();
                        logger.info("Audio analysis finished");
                    } catch (Exception e) {
                        displayErrorMessage(MainWindow.this, e);
                    }
                }
            });

            AudioFile audio = proc.getFile();
            logger.info("Playing...");
            player.play(audio);
        } catch (Exception e) {
            displayErrorMessage(this, e);
        }
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
        JFileChooser chooser = new JFileChooser(DIR);
        FileFilter filter = new FileNameExtensionFilter("Audio", EXTS);
        chooser.setFileFilter(filter);
        int ret = chooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }

    private void setupUI() {
        setupPosition();
        setupMenu();
        setupLayout();
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
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
        presetMenu = new JMenu("Preset");
        addPreset(presetMenu, "Invention no. 8 (Bach)", FILE);
        addPreset(presetMenu, "Symphony no. 9 (Beethoven)", BIG);
        addPreset(presetMenu, "Sonata no. 30 Prestissimo (Beethoven)", BEETH);
        addPreset(presetMenu, "Sonata no. 1 Allegro Molto (Rach)", RACH);

        menuBar.add(fileMenu);
        menuBar.add(presetMenu);
        setJMenuBar(menuBar);
    }

    private void addPreset(JMenu menu, String name, final String path) {
        menu.add(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile(new File(path));
            }
        });
    }

    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1.0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridy = 0;
        add(waveView, c);
        ++c.gridy;
        // add(powerPanel, c);
        ++c.gridy;
        // add(spectrumPanel, c);

        ++c.gridy;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        add(Box.createVerticalGlue(), c);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainWindow("GriegCounter");
                } catch (ConfigException e) {
                    displayErrorMessage(null, e);
                }
            }
        });
    }

    private class ClosingListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            logger.debug("Window closing");
        }
    }

}
