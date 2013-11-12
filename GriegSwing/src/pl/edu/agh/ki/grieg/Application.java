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
import pl.edu.agh.ki.grieg.processing.model.WaveWindowModel;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratee;

public class Application implements Controller{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CompositeModel<?> modelRoot;

    protected final Scheduler scheduler;
    protected final Player player;
    protected final ProcessorFactory procFactory;
    
    protected final ErrorHandler errorHandler;

    {
        player = new Player();
        modelRoot = Models.container();
    }

    public Application(Bootstrap bootstrap, ErrorHandler errorHandler) throws ConfigException {
        
        this.errorHandler = errorHandler;
        this.scheduler = new Scheduler(errorHandler);

        procFactory = bootstrap.createFactory();

        CompositeModel<?> preanalysis = Models.container();
        modelRoot.addModel("preanalysis", preanalysis);
        
        final FeatureExtractionModel extractionModel =
                new FeatureExtractionModel(TimeUnit.MILLISECONDS, 20);
        preanalysis.addModel("progress", extractionModel.getModel());

        CompositeModel<?> waveModel = Models.container();
        modelRoot.addModel("wave", waveModel);
        
        AudioModel model = new AudioModel();
        procFactory.addListener(model);
        waveModel.addModel("amplitude", model.getModel());

        WaveFunctionModel powerModel = new WaveFunctionModel("power");
        procFactory.addListener(powerModel);
        waveModel.addModel("power", powerModel.getModel());
        
        
        CompositeModel<?> waveWindows = Models.container();
        waveModel.addModel("window", waveWindows);
        
        WaveWindowModel waveWindow = new WaveWindowModel(3000);
        procFactory.addListener(waveWindow);
        waveWindows.addModel("narrow", waveWindow.getModel());
        
        WaveWindowModel wideWaveWindow = new WaveWindowModel(30000);
        procFactory.addListener(wideWaveWindow);
        waveWindows.addModel("wide", wideWaveWindow.getModel());
        

        CompositeModel<?> fftModel = Models.container();
        waveModel.addModel("fft", fftModel);
        
        IterateeWrapper<float[]> fftReal = IterateeWrapper.of(float[].class);
        connect(fftReal, float[].class, "fft_real");
        fftModel.addModel("real", fftReal.getModel());

        IterateeWrapper<float[]> powerSpectrumModel = IterateeWrapper.of(float[].class);
        connect(powerSpectrumModel, float[].class, "power_spectrum");
        fftModel.addModel("power", powerSpectrumModel.getModel());
        
        CompositeModel<?> systemModel = Models.container();
        modelRoot.addModel("sys", systemModel);
        
        CompositeModel<?> loader = Models.container();
        systemModel.addModel("loader", loader);

        FileLoader fileLoader = procFactory.getFileLoader();
        Set<String> extensions = fileLoader.getKnownExtensions();
        loader.addModel("extensions", Models.simple(extensions));

        procFactory.addListener(new ProcessingAdapter() {
            
            @Override
            public void beforePreAnalysis(ExtractionContext ctx) {
                ctx.addFeaturesListener(extractionModel);
                ctx.addProgressListener(extractionModel);
            }
            
            @Override
            public void beforeAnalysis(Pipeline<float[][]> pipeline,
                    SampleEnumerator source) {
                try {
                    player.prepare(source);
                } catch (PlaybackException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private <T> void connect(final Iteratee<? super T> it,
            final Class<T> clazz, final String input) {
        procFactory.addListener(new ProcessingAdapter() {
            @Override
            public void beforeAnalysis(Pipeline<float[][]> pipeline,
                    SampleEnumerator source) {
                pipeline.connect(it, clazz).to(input);
            }
        });
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

            // AudioFile audio = proc.getFile();
            // logger.info("Playing...");
            // player.play(audio);
        } catch (Exception e) {
            errorHandler.error(e);
        }
    }

}
