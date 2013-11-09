package pl.edu.agh.ki.grieg;

import java.io.File;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.io.FileLoader;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.model.CompositeModel;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.model.Models;
import pl.edu.agh.ki.grieg.playback.PlaybackException;
import pl.edu.agh.ki.grieg.playback.Player;
import pl.edu.agh.ki.grieg.processing.core.Bootstrap;
import pl.edu.agh.ki.grieg.processing.core.ProcessingAdapter;
import pl.edu.agh.ki.grieg.processing.core.Processor;
import pl.edu.agh.ki.grieg.processing.core.ProcessorFactory;
import pl.edu.agh.ki.grieg.processing.core.config.ConfigException;
import pl.edu.agh.ki.grieg.processing.model.AudioModel;
import pl.edu.agh.ki.grieg.processing.model.FeatureExtractionModel;
import pl.edu.agh.ki.grieg.processing.model.IterateeWrapper;
import pl.edu.agh.ki.grieg.processing.model.WaveFunctionModel;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
    

public class Application implements Controller, ErrorHandler {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final CompositeModel<?> modelRoot;
    
    protected final Scheduler scheduler;
    protected final Player player;
    protected final ProcessorFactory procFactory;

    {
        scheduler = new Scheduler(this);
        player = new Player();
        modelRoot = Models.container();
    }
    
    public Application(Bootstrap bootstrap) throws ConfigException {

        procFactory = bootstrap.createFactory();
        
        final FeatureExtractionModel extractionModel = 
                new FeatureExtractionModel(TimeUnit.MILLISECONDS, 20);
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
        
        WaveFunctionModel powerModel = new WaveFunctionModel("power");
        procFactory.addListener(powerModel);
        modelRoot.addModel("power", powerModel.getModel());
        
        
        final IterateeWrapper<float[]> fftModel = IterateeWrapper.of(float[].class);
        procFactory.addListener(new ProcessingAdapter() {
            @Override
            public void beforeAnalysis(Pipeline<float[][]> pipeline, SampleEnumerator source) {
                pipeline.connect(fftModel, float[].class).to("fft_real");
                try {
                    player.prepare(source);
                } catch (PlaybackException e) {
                    e.printStackTrace();
                }
            }
        });
        modelRoot.addModel("fft", fftModel.getModel());
        
        CompositeModel<?> loader = Models.container();
        FileLoader fileLoader = procFactory.getFileLoader();
        Set<String> extensions = fileLoader.getKnownExtensions();
        loader.addModel("extensions", Models.simple(extensions));
        modelRoot.addModel("loader", loader);
    }
    
    @Override
    public void error(Throwable e) {
        logger.error("Error", e);
    }
    
    protected Player player() {
        return player;
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
            
            scheduler.asyncPreAnalysis(proc);
            scheduler.asyncAnalysis(proc);

//            AudioFile audio = proc.getFile();
//            logger.info("Playing...");
//            player.play(audio);
        } catch (Exception e) {
            error(e);
        }
    }


}
