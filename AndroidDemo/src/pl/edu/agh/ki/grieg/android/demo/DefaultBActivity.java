package pl.edu.agh.ki.grieg.android.demo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.edu.agh.ki.grieg.android.misc.Dictionary;
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
import pl.edu.agh.ki.grieg.util.ProgressListener;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.util.math.Point;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class DefaultBActivity extends RoboActivity {

	private static final Logger logger = LoggerFactory
			.getLogger(GriegMain.class);

	private final ExecutorService executor = Executors
			.newSingleThreadExecutor();

	private String file_chosen = "";

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
	
	ProgressDialog pd;

	WaveWindowModel waveWindow;

	WaveWindowModel wideWaveWindow;

	private CompositeModel<?> modelRoot;

	private boolean onlyOffline;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_default_b);
		Bundle extras = getIntent().getExtras();
		file_chosen = (String) extras.get(Dictionary.FILE_CHOSEN.getCaption());
		SharedPreferences sharedPreferences = getSharedPreferences(
				Dictionary.PREFS.getCaption(), Context.MODE_PRIVATE);
		onlyOffline = sharedPreferences.getBoolean(
				Dictionary.ONLY_OFFLINE.getCaption(), false);
		// TABS//
		TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();

		TabSpec wave = tabHost.newTabSpec("Tab 1");
		wave.setContent(R.id.tab1);
		wave.setIndicator("Tab 1");

		TabSpec spectral = tabHost.newTabSpec("Tab 3");
		spectral.setIndicator("Tab 3");
		spectral.setContent(R.id.tab2);

		TabSpec power = tabHost.newTabSpec("Tab 2");
		power.setIndicator("Tab 2");
		power.setContent(R.id.tab4);

		TabSpec window = tabHost.newTabSpec("Tab 4");
		window.setIndicator("Tab 4");
		window.setContent(R.id.tab5);

		tabHost.setCurrentTab(0);
		if(onlyOffline){
			factory = getGrieg().getOfflineFactory();
		}
		else{
			factory = getGrieg().getFullFactory();
		}
		
		
		//Progress bar!
				pd = new ProgressDialog(this);
				pd.setTitle("Processing...");
				pd.setMessage("Please wait.");
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.setCancelable(false);
				pd.setIndeterminate(false);
				pd.show();
		//END progress bar

		modelRoot = Models.container();
		createModels();
		Class<? extends List<Point>> clazz = Reflection.castClass(List.class);

		waveLeftChannel.setModel(modelRoot.getChild("wave.left", clazz));
		waveRightChannel.setModel(modelRoot.getChild("wave.right", clazz));
		tabHost.addTab(wave);
		power_left.setModel(modelRoot.getChild("power.left", clazz));
		power_left.setZeroToOneScale();
		power_right.setModel(modelRoot.getChild("power.right", clazz));
		power_right.setZeroToOneScale();
		tabHost.addTab(power);

		if (!onlyOffline) {
			narrow.setModel(modelRoot.getChild("window.narrow.left", clazz));
			wide.setModel(modelRoot.getChild("window.wide.left", clazz));
			tabHost.addTab(spectral);
			spectrum.setModel(modelRoot.getChild("fft.power", float[].class));
			tabHost.addTab(window);
		}

		try {
			startProcessing();
		} catch (Exception e) {
			logger.error("Error", e);
		}

	}

	private void createModels() {

		SharedPreferences sharedPreferences = getSharedPreferences(
				Dictionary.PREFS.getCaption(), Context.MODE_PRIVATE);
		if (!onlyOffline) {
			CompositeModel<?> waveWindows = Models.container();
			modelRoot.addModel("window", waveWindows);

			waveWindow = new WaveWindowModel(3000);
			connect(waveWindow, float[][].class, "<root>");
			waveWindows.addModel("narrow", waveWindow.getModel());

			wideWaveWindow = new WaveWindowModel(30000);
			connect(wideWaveWindow, float[][].class, "<root>");
			waveWindows.addModel("wide", wideWaveWindow.getModel());

			CompositeModel<?> fftModel = Models.container();
			modelRoot.addModel("fft", fftModel);

			IterateeWrapper<float[]> powerSpectrumModel = IterateeWrapper
					.of(float[].class);
			connect(powerSpectrumModel, float[].class, "power_spectrum");
			fftModel.addModel("power", powerSpectrumModel.getModel());
		}

		AudioModel model = new AudioModel();
		factory.addListener(model);

		Model<?> m = model.getModel();
		modelRoot.addModel("wave", m);

		WaveFunctionModel powerModel = new WaveFunctionModel("power");
		factory.addListener(powerModel);
		modelRoot.addModel("power", powerModel.getModel());

		CompositeModel<?> preanalysis = Models.container();
		modelRoot.addModel("preanalysis", preanalysis);

		final FeatureExtractionModel extractionModel = new FeatureExtractionModel(
				TimeUnit.MILLISECONDS, 20);
		preanalysis.addModel("progress", extractionModel.getModel());

		factory.addListener(new ProcessingAdapter() {

			@Override
			public void beforePreAnalysis(ExtractionContext ctx) {
				ctx.addFeaturesListener(extractionModel);
				ctx.addProgressListener(extractionModel);
				ctx.addProgressListener(new ProgressListener() {
					
					@Override
					public void started() {
						pd.setProgress(0);
						
					}
					
					@Override
					public void progress(float percent) {
						logger.debug(String.valueOf(percent));
						pd.setProgress((int)(percent*100));
						
						
					}
					
					@Override
					public void finished() {
						logger.debug("pre finished");
						pd.dismiss();
						
					}
					
					@Override
					public void failed(Exception e) {
						logger.debug(e.getLocalizedMessage());
						
					}
				});
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
			catch(Throwable t){
				logger.error("zlee", t);
			}
		}
	}

	private void startProcessing() throws AudioException, IOException {
		final Processor proc = factory.newFileProcessor(new File(file_chosen));
		try{
			proc.openFile();
			enqueue(new PreAnalysis(proc));
			enqueue(new Analysis(proc));
		}
		catch(Throwable t){
			logger.error("zleee");
			logger.error("here", t);
		}
	}

	private void enqueue(Runnable action) {
		executor.execute(action);
		// action.run();
	}

	private GriegApplication getGrieg() {
		return (GriegApplication) getApplication();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	        finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onDestroy(){
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}

}
