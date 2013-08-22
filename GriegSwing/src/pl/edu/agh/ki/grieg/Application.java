package pl.edu.agh.ki.grieg;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.gui.swing.Controller;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.FileLoader;
import pl.edu.agh.ki.grieg.model.CompositeModel;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.model.Models;
import pl.edu.agh.ki.grieg.playback.Player;
import pl.edu.agh.ki.grieg.processing.core.Bootstrap;
import pl.edu.agh.ki.grieg.processing.core.ProcessingAdapter;
import pl.edu.agh.ki.grieg.processing.core.Processor;
import pl.edu.agh.ki.grieg.processing.core.ProcessorFactory;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.model.AudioModel;
import pl.edu.agh.ki.grieg.processing.model.FeatureExtractionModel;
    

public class Application implements Controller {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final CompositeModel<?> modelRoot;
    private final ExecutorService executor;

    protected final Player player;
    protected final ProcessorFactory procFactory;

    {
        player = new Player();
        modelRoot = Models.container();
        executor = Executors.newSingleThreadExecutor();
    }
    
    public Application(Bootstrap bootstrap) throws ConfigException {

        procFactory = bootstrap.createFactory();
        
        final FeatureExtractionModel extractionModel = 
                new FeatureExtractionModel(TimeUnit.MILLISECONDS, 100);
        procFactory.addListener(new ProcessingAdapter() {
            @Override
            public void beforePreAnalysis(ExtractionContext ctx) {
                ctx.addFeaturesListener(extractionModel);
                ctx.addProgressListener(extractionModel);
            }
        });
        modelRoot.addModel("preanalysis_progress", extractionModel.getModel());
        
        AudioModel model = new AudioModel();
        procFactory.addListener(model);
        modelRoot.addModel("wave", model.getModel());
        
        CompositeModel<?> loader = Models.container();
        FileLoader fileLoader = procFactory.getFileLoader();
        Set<String> extensions = fileLoader.getKnownExtensions();
        loader.addModel("extensions", Models.simple(extensions));
        modelRoot.addModel("loader", loader);
    }
    
    protected void handleError(Throwable e) {
        logger.error("Error", e);
    }
    
    protected Player player() {
        return player;
    }
    
    protected void enqueue(Runnable action) {
        executor.execute(action);
    }
    
    public Model<?> getModelRoot() {
        return modelRoot;
    }
    
    @Override
    public void processFile(File file) {
        String path = file.getAbsolutePath();
        logger.info("Opening file {}", path);
        try {
            final Processor proc = procFactory.newFileProcessor(file);
            proc.openFile();
            
            enqueue(preAnalysisTask(proc));
            enqueue(analysisTask(proc));

            AudioFile audio = proc.getFile();
            logger.info("Playing...");
            player.play(audio);
        } catch (Exception e) {
            handleError(e);
        }
    }
    
    protected Runnable preAnalysisTask(Processor proc) {
        return new PreAnalysis(proc);
    }
    
    protected Runnable analysisTask(Processor proc) {
        return new Analysis(proc);
    }
    
    private final class PreAnalysis implements Runnable {
        private final Processor proc;

        private PreAnalysis(Processor proc) {
            this.proc = proc;
        }

        @Override
        public void run() {
            logger.info("Gathering metadata");
            try {
                proc.preAnalyze();
                logger.info("Metadata gathered");
            } catch (AudioException e) {
                handleError(e);
                logger.error("Error during preliminary analysis", e);
            } catch (IOException e) {
                handleError(e);
                logger.error("Error during preliminary analysis", e);
            }
        }
    }
    
    private final class Analysis implements Runnable {
        private final Processor proc;

        private Analysis(Processor proc) {
            this.proc = proc;
        }

        @Override
        public void run() {
            try {
                logger.info("Beginning audio analysis");
                proc.analyze();
                logger.info("Audio analysis finished");
            } catch (AudioException e) {
                handleError(e);
                logger.error("Error during the main analysis phase", e);
            } catch (IOException e) {
                handleError(e);
                logger.error("Error during the main analysis phase", e);
            }
        }
    }

}
