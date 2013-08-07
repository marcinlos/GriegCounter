package pl.edu.agh.ki.grieg.android.demo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.model.ChartModel;
import pl.edu.agh.ki.grieg.model.Serie;
import pl.edu.agh.ki.grieg.processing.core.Processor;
import pl.edu.agh.ki.grieg.processing.core.ProcessorFactory;
import pl.edu.agh.ki.grieg.processing.model.AudioModel;
import pl.edu.agh.ki.grieg.util.Point;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.Menu;

public class GriegMain extends RoboActivity {
    
    private static final Logger logger = LoggerFactory.getLogger(GriegMain.class);
    
    private ProcessorFactory factory;
    
    @InjectView(R.id.leftChannel)
    private LineChartView leftChannel;

    @InjectView(R.id.rightChannel)
    private LineChartView rightChannel;
    
    
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    private static final String BACH = "/data/user/Invention no. 8 (F major).mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grieg_main);
        
        logger.debug("GriegMain activity created");
        
        factory = getGrieg().getFactory();
        
        AudioModel model = new AudioModel();
        factory.addListener(model);
        
        
        ChartModel<List<Point>> chart = model.getChartModel();
        Serie<List<Point>> leftSerie = chart.getSerie("left");
        Serie<List<Point>> rightSerie = chart.getSerie("right");
        
        rightChannel.setData(rightSerie.getData());
        leftChannel.setData(leftSerie.getData());
        
        rightSerie.addListener(rightChannel);
        leftSerie.addListener(leftChannel);
        
        startProcessing();
    }

    private void startProcessing() {
        try {
            final Processor proc = factory.newFileProcessor(new File(BACH));
            proc.openFile();
            enqueue(new PreAnalysis(proc));
            enqueue(new Analysis(proc));
        } catch (AudioException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.grieg_main, menu);
        return true;
    }
    
    private GriegApplication getGrieg() {
        return (GriegApplication) getApplication(); 
    }

}
