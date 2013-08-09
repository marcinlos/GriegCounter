package pl.edu.agh.ki.grieg.gui.swing;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainWindow window = new MainWindow("GriegCounter");
                    window.pack();
                    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    window.setVisible(true);
                } catch (ConfigException e) {
                    logger.error("Configuration exception", e);
                    Dialogs.showError(e);
                } catch (Exception e) {
                    logger.error("Fatal error", e);
                    Dialogs.showError(e);
                }
            }
        });
    }

}
