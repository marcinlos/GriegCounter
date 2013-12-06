package pl.edu.agh.ki.grieg.android.demo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.android.misc.FolderPicker;
import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.model.CompositeModel;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.model.Models;
import pl.edu.agh.ki.grieg.processing.core.ProcessingAdapter;
import pl.edu.agh.ki.grieg.processing.core.Processor;
import pl.edu.agh.ki.grieg.processing.core.ProcessorFactory;
import pl.edu.agh.ki.grieg.processing.model.AudioModel;
import pl.edu.agh.ki.grieg.processing.model.FeatureExtractionModel;
import pl.edu.agh.ki.grieg.processing.model.IterateeWrapper;
import pl.edu.agh.ki.grieg.processing.model.WaveFunctionModel;
import pl.edu.agh.ki.grieg.processing.model.WaveWindowModel;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.util.math.Point;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class GriegMain extends RoboActivity implements OnClickListener {
    
    private static final Logger logger = LoggerFactory.getLogger(GriegMain.class);
    
    private ProcessorFactory factory;
    
    @InjectView(R.id.leftChannel)
    private LineChartView waveLeftChannel;

    @InjectView(R.id.rightChannel)
    private LineChartView waveRightChannel;
    
    @InjectView(R.id.narrow_wave)
    private LineChartView narrow;
    
    @InjectView(R.id.wide_wave)
    private LineChartView wide;
    
    @InjectView(R.id.power_left)
    private LineChartView power_left;
    
    @InjectView(R.id.power_right)
    private LineChartView power_right;
    
    @InjectView(R.id.spectrum)
    private SpectrumView spectrum;
    
    WaveWindowModel waveWindow;
    
    WaveWindowModel wideWaveWindow;
    
    private CompositeModel<?> modelRoot;
    
    
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    private String BACH = "/data/user/Alright.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grieg_main);
        
        //TABS//
        TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabSpec spec1=tabHost.newTabSpec("Tab 1");
        spec1.setContent(R.id.tab3);
        spec1.setIndicator("Tab 1");

        TabSpec spec2=tabHost.newTabSpec("Tab 2");
        spec2.setIndicator("Tab 2");
        spec2.setContent(R.id.tab2);
        
        TabSpec spec3=tabHost.newTabSpec("Tab 3");
        spec3.setIndicator("Tab 3");
        spec3.setContent(R.id.LinearLayout1);
        
        TabSpec spec4=tabHost.newTabSpec("Tab 4");
        spec4.setIndicator("Tab 4");
        spec4.setContent(R.id.tab4);
        
        TabSpec spec5=tabHost.newTabSpec("Tab 5");
        spec5.setIndicator("Tab 5");
        spec5.setContent(R.id.tab5);

        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);
        tabHost.addTab(spec4);
        tabHost.addTab(spec5);
        
        tabHost.setCurrentTab(0);
        tabHost.setCurrentTab(1);
        tabHost.setCurrentTab(2);
        tabHost.setCurrentTab(0);
        //ENDTABS//
        
        logger.debug("GriegMain activity created");
        
        factory = getGrieg().getFullFactory();
        
        
        modelRoot = Models.container();
        createModels();
        Class<? extends List<Point>> clazz = Reflection.castClass(List.class);
        waveLeftChannel.setModel(modelRoot.getChild("wave.left", clazz));
        waveRightChannel.setModel(modelRoot.getChild("wave.right", clazz));
        narrow.setModel(modelRoot.getChild("window.narrow.left", clazz));
        wide.setModel(modelRoot.getChild("window.wide.left", clazz));
        power_left.setModel(modelRoot.getChild("power.left", clazz));
        power_left.setZeroToOneScale();
        power_right.setModel(modelRoot.getChild("power.right", clazz));
        power_right.setZeroToOneScale();
        spectrum.setModel(modelRoot.getChild("fft.power", float[].class));

        
        /*
        try {
        	startProcessing();
        } catch (Exception e) {
        	logger.error("Error", e);
        }*/
        
    }
    
    private void createModels(){
    	AudioModel model = new AudioModel();
        factory.addListener(model);
        
        Model<?> m = model.getModel();
        modelRoot.addModel("wave", m);
        
        
        CompositeModel<?> waveWindows = Models.container();
        modelRoot.addModel("window", waveWindows);
        
        waveWindow = new WaveWindowModel(3000);
        factory.addListener(waveWindow);
        waveWindows.addModel("narrow", waveWindow.getModel());
        
        wideWaveWindow = new WaveWindowModel(30000);
        factory.addListener(wideWaveWindow);
        waveWindows.addModel("wide", wideWaveWindow.getModel());
        
        WaveFunctionModel powerModel = new WaveFunctionModel("power");
        factory.addListener(powerModel);
        modelRoot.addModel("power", powerModel.getModel());
        
        CompositeModel<?> fftModel = Models.container();
        modelRoot.addModel("fft", fftModel);
        
        IterateeWrapper<float[]> powerSpectrumModel = IterateeWrapper.of(float[].class);
        connect(powerSpectrumModel, float[].class, "power_spectrum");
        fftModel.addModel("power", powerSpectrumModel.getModel());
        
        
        
        CompositeModel<?> preanalysis = Models.container();
        modelRoot.addModel("preanalysis", preanalysis);
        
        final FeatureExtractionModel extractionModel =
                new FeatureExtractionModel(TimeUnit.MILLISECONDS, 20);
        preanalysis.addModel("progress", extractionModel.getModel());
        
        factory.addListener(new ProcessingAdapter() {
          
            @Override
            public void beforePreAnalysis(ExtractionContext ctx) {
                ctx.addFeaturesListener(extractionModel);
                ctx.addProgressListener(extractionModel);
            }
        });

    }
    
    private <T> void connect(final Iteratee<? super T> it,
            final Class<T> clazz, final String input) {
        factory.addListener(new ProcessingAdapter() {
            @Override
            public void beforeAnalysis(Pipeline<float[][]> pipeline,
                    SampleEnumerator source) {
                pipeline.connect(it, clazz).to(input);
            }
        });
    }
    

	private void startProcessing() throws AudioException, IOException {
        final Processor proc = factory.newFileProcessor(new File(BACH));
        proc.openFile();
        enqueue(new PreAnalysis(proc));
        enqueue(new Analysis(proc));
    }
    
    private void enqueue(Runnable action) {
        executor.execute(action);
//        action.run();
    }
    
    private final class PreAnalysis implements Runnable {
        private final Processor proc;

        PreAnalysis(Processor proc) {
            this.proc = proc;
        }

        @Override
        public void run() {
            logger.info("Gathering metadata");
            try {
                proc.preAnalyze();
                logger.info("Metadata gathered");
            } catch (Exception e) {
                logger.error("Error during preliminary analysis", e);
            }
        }
    }
    
    private final class Analysis implements Runnable {
        private final Processor proc;

        Analysis(Processor proc) {
            this.proc = proc;
        }

        @Override
        public void run() {
            try {
                logger.info("Beginning audio analysis");
                proc.analyze();
                logger.info("Audio analysis finished");
            } catch (Exception e) {
                logger.error("Error during the main analysis phase", e);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
        // Inflate the menu; this adds items to the action bar if it is present
    	
        getMenuInflater().inflate(R.menu.grieg_main, menu);
        return true;
    }
    
    private GriegApplication getGrieg() {
        return (GriegApplication) getApplication(); 
    }
    
    public void openFile(View v){
    	FolderPicker p = new FolderPicker(this, this, 0, true);
    	p.show();
    }

	@Override
	public void onClick(DialogInterface dialog, int which) {
		FolderPicker picker = (FolderPicker) dialog;
		if(which == DialogInterface.BUTTON_POSITIVE){
			picker.dismiss();
			BACH = picker.getPath();
			logger.error(BACH);
			try {
	        	startProcessing();
	        } catch (Exception e) {
	        	logger.error("Error", e);
	        }
			
		}
		
		
	}

}
