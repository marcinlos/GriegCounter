package pl.edu.agh.ki.grieg.processing.model;

import pl.edu.agh.ki.grieg.features.AudioFeatures;
import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.model.SimpleModel;
import pl.edu.agh.ki.grieg.processing.core.ProcessingListener;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;

public class DupaModel extends SimpleModel<String> implements ProcessingListener, Iteratee<float[]> {

	public DupaModel(String data) {
		super("dupa");
		// TODO Auto-generated constructor stub
	}

	@Override
	public State step(float[] item) {
		//change item into dupa
		update();
		System.out.println("dupa_step");
		return State.Cont;
	}

	@Override
	public void finished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fileOpened(AudioFile file, Properties config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforePreAnalysis(ExtractionContext ctx) {
		// TODO Auto-generated method stub
		ctx.shouldCompute(AudioFeatures.DURATION);
		
	}

	@Override
	public void afterPreAnalysis(Properties results) {
		// TODO Auto-generated method stub
		Float f = results.get(AudioFeatures.DURATION);
		if(f != null){
			update("dupa" + f.toString());
		}
		
	}

	@Override
	public void beforeAnalysis(Pipeline<float[][]> pipeline) {
		// TODO Auto-generated method stub
		System.out.println("dupa_befA");
		pipeline.connect(this, float[].class).to("skipper");
	}

	@Override
	public void afterAnalysis() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failed(Throwable e) {
		// TODO Auto-generated method stub
		update("wyj¹tek " + e + " dupa");
	}

}
