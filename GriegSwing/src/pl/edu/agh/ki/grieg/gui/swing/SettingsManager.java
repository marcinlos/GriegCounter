package pl.edu.agh.ki.grieg.gui.swing;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SettingsManager {

    private static final Logger logger = LoggerFactory
            .getLogger(SettingsManager.class);
    
    private static final String CWDIR = "user.dir";
    
    private final Gson gson;
    
    private final File file;

    public SettingsManager(File file) {
        this.file = file;
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Settings readSettings() {
        Settings settings;
        String path = file.getAbsolutePath();
        logger.info("Attempting to read settings from {}", path);
        try (Reader config = new FileReader(file)) {
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

    public void logSettings(Settings settings) {
        File dir = new File(settings.getDirectory());
        logger.debug("  dir = {}", dir.getAbsolutePath());
        logger.debug("  files = {");
        for (String file : settings.getFiles()) {
            logger.debug("    {}", file);
        }
        logger.debug("  }");
    }

    public Settings createDefaultSettings() {
        String path = System.getProperty(CWDIR);
        return new Settings(path);
    }

}
