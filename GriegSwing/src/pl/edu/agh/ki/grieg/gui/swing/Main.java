package pl.edu.agh.ki.grieg.gui.swing;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.Application;
import pl.edu.agh.ki.grieg.model.observables.Model;
import pl.edu.agh.ki.grieg.processing.core.Bootstrap;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.core.config.XmlFileSystemBootstrap;

public class Main implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    private void init() throws ConfigException {
        Bootstrap bootstrap = new XmlFileSystemBootstrap("grieg-config.xml");
        Application app = new Application(bootstrap);
        Model<?> model = app.getModelRoot();
        MainWindow window = new MainWindow(model, app, "GriegCounter");
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    @Override
    public void run() {
        try {
            init();
        } catch (ConfigException e) {
            logger.error("Configuration exception", e);
            Dialogs.showError(e);
        } catch (Exception e) {
            logger.error("Fatal error", e);
            Dialogs.showError(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main());
    }

}
