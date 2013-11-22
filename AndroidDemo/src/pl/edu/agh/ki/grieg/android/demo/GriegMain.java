package pl.edu.agh.ki.grieg.android.demo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.android.misc.FolderPicker;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.model.CompositeModel;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.model.Models;
import pl.edu.agh.ki.grieg.processing.core.Processor;
import pl.edu.agh.ki.grieg.processing.core.ProcessorFactory;
import pl.edu.agh.ki.grieg.processing.model.AudioModel;
import pl.edu.agh.ki.grieg.processing.model.WaveWindowModel;
import pl.edu.agh.ki.grieg.util.Reflection;
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
    private LineChartView leftChannel;

    @InjectView(R.id.rightChannel)
    private LineChartView rightChannel;
    
    @InjectView(R.id.narrow_wave)
    private LineChartView narrow;
    
    @InjectView(R.id.wide_wave)
    private LineChartView wide;
    
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
        spec1.setContent(R.id.LinearLayout1);
        spec1.setIndicator("Tab 1");

        TabSpec spec2=tabHost.newTabSpec("Tab 2");
        spec2.setIndicator("Tab 2");
        spec2.setContent(R.id.tab2);
        

        TabSpec spec3=tabHost.newTabSpec("Tab 3");
        spec3.setIndicator("Tab 3");
        spec3.setContent(R.id.tab3);

        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);
        
        tabHost.setCurrentTab(0);
        tabHost.setCurrentTab(1);
        tabHost.setCurrentTab(2);
        tabHost.setCurrentTab(0);
        //ENDTABS//
        
        logger.debug("GriegMain activity created");
        
        factory = getGrieg().getFactory();
        
        modelRoot = Models.container();
        
        AudioModel model = new AudioModel();
        factory.addListener(model);
        
        Model<?> m = model.getModel();
        modelRoot.addModel("wave", m);
        
        Class<? extends List<Point>> clazz = Reflection.castClass(List.class);
        Model<List<Point>> leftSerie = m.getChild("left", clazz);
        Model<List<Point>> rightSerie = m.getChild("right", clazz);
        leftChannel.setModel(leftSerie);
        rightChannel.setModel(rightSerie);
        
        
        CompositeModel<?> waveWindows = Models.container();
        modelRoot.addModel("window", waveWindows);
        
        waveWindow = new WaveWindowModel(3000);
        factory.addListener(waveWindow);
        waveWindows.addModel("narrow", waveWindow.getModel());
        Model<List<Point>> castTmp = (Model<List<Point>>) waveWindow.getModel().getChild("left", clazz);
        narrow.setModel(castTmp);
        
        wideWaveWindow = new WaveWindowModel(30000);
        factory.addListener(wideWaveWindow);
        waveWindows.addModel("wide", wideWaveWindow.getModel());
        castTmp = (Model<List<Point>>) wideWaveWindow.getModel().getChild("left", clazz);
        wide.setModel(castTmp);

        
        /*
        try {
        	startProcessing();
        } catch (Exception e) {
        	logger.error("Error", e);
        }*/
        
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
