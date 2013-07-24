package pl.edu.agh.ki.grieg.console;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.processing.core.Analyzer;

public class Main {
    
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    private final FileLoader loader = new FileLoader();
    
    private final Analyzer analyzer = new Analyzer(loader);
    
    
    
    
    
    public void process(File file) {
        logger.info("Processing file {}", file);
        analyzer.newProcessing(file);
    }
    
    public void process(String path) {
        process(new File(path));
    }
    
    
    public static void main(String[] args) {
        Main main = new Main();
        main.process(Pieces.BACH);
    }

}
