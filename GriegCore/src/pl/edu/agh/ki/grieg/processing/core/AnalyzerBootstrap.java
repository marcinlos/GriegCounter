package pl.edu.agh.ki.grieg.processing.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.util.PropertyMap;

public class AnalyzerBootstrap {

    private static final Logger logger = LoggerFactory
            .getLogger(AnalyzerBootstrap.class);

    public AnalyzerBootstrap() {
    }

    public Analyzer createAnalyzer() {
        AnalyzerConfig config = new AnalyzerConfig();
        config.setFileLoader(new FileLoader());
        config.setProperties(new PropertyMap());
        config.setPipelineFactory(new DefaultPipelineFactory());
        return new Analyzer(config);
    }

}
