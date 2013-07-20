package pl.edu.agh.ki.grieg.swing.test.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;

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

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.playback.Player;

public class MainWindow extends JFrame {
    
    private static final int BUFFER_SIZE = 8192;
    
    private FileLoader fileLoader = FileLoader.getInstance();
    private Player player = new Player(fileLoader, BUFFER_SIZE);

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu presetMenu;
    private WavePanel wavePanel;
    private TwoChannelPannel powerPanel;
    private SpectrumPanel spectrumPanel;
    
    private static final String DIR = "/media/los/Data/muzyka/Klasyczna/";
    private static final String FILE = DIR + "Bach/Inventions/Invention no. 8 (F major).mp3";
    private static final String BEETH = DIR + "Beethoven/Op. 109 (PS no. 30 in E major)/Piano Sonata no.30 in E major op.109 - II- Prestissimo.mp3";
    private static final String BIG = DIR +"Beethoven/Beethoven's 9th.mp3";
    private static final String RACH = DIR + "Rachmaninov/Op. 28 (PS no. 1 in D minor)/03 Piano Sonata No.1 in D minor Op.28 - III. Allegro molto.mp3";
    
    private static final String[] EXTS = { "mp3", "wav" };
    
    public MainWindow(String label) {
        super(label);
        
        /*FFT fft = new FFT();
        player.addAnalysis(fft);
        PowerSpectrum ps = new PowerSpectrum();
        fft.addListener(ps);
        SpectralDifference sd = new SpectralDifference(ac.getSampleRate());
        ps.addListener(sd);
        PeakDetector beatDetector = new PeakDetector();
        sd.addListener(beatDetector);
        beatDetector.setThreshold(0.2f);
        beatDetector.setAlpha(.3f);
        beatDetector.addMessageListener(new Bead() {
            protected void messageReceived(Bead b) {
                //System.out.println("SPAAAAC");
            }
        });*/
        
        powerPanel = new PowerPanel(/*player*/);
        wavePanel = new WavePanel(/*player*/);
        spectrumPanel = new SpectrumPanel(/*player, ps*/);
        
        getContentPane().setBackground(Color.black);
        setupUI();
    }

    private void openFile(File file) {
        try {
            player.play(file);
        } catch (Exception e) {
            displayErrorMessage(e);
        }
        /*player.stop();
        TrackLoader.loadAsync(file, new LoadCallback() {

            @Override 
            public void failed(Throwable e) {
                JOptionPane.showMessageDialog(MainWindow.this, e, "Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
            
            @Override
            public void completed(Track track) {
                wavePanel.setTrack(track);
                powerPanel.setTrack(track);
                player.play(track);
            }
        });*/
    }
    
    private void displayErrorMessage(Throwable e) {
        JOptionPane.showMessageDialog(MainWindow.this, e, "Error", 
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
        add(wavePanel, c);
        ++ c.gridy;
        add(powerPanel, c);
        ++ c.gridy;
        add(spectrumPanel, c);
        
        ++ c.gridy;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        add(Box.createVerticalGlue(), c);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow("GriegCounter");
            }
        });
    }

}
