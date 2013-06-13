package com.grieg.gui;


import java.util.Random;

import com.grieg.gui.R;
import com.grieg.interfaces.IAnalysisProvider;
import com.grieg.listeners.PowerListener;
import com.grieg.listeners.WaveListener;
import com.grieg.views.DrawView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

public class SoundAnalyzer extends Activity implements WaveListener, PowerListener {
	
	public final static String EXTRA_MESSAGE = "sound.to.analyze";
	public final static String ID = "Analyzer";
	private String filepath;
	private boolean firstFocus = true;
	private IAnalysisProvider anylser;
	private DrawView firstChannelWave;
	private DrawView secondChannelWave;
	private DrawView firstChannelPower;
	private DrawView secondChannelPower;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(ID,"I'm");
		super.onCreate(savedInstanceState);
		Log.e(ID,"I'm starting");
		setContentView(R.layout.activity_sound_analyzer);
		Intent intent = getIntent();
		filepath = intent.getStringExtra(EXTRA_MESSAGE);
		Log.e(ID,"I'm mostly started");
		firstChannelWave = (DrawView) this.findViewById(R.id.myDrawView1);
		secondChannelWave = (DrawView) this.findViewById(R.id.myDrawView2);
		firstChannelPower = (DrawView) this.findViewById(R.id.myDrawView3);
		secondChannelPower = (DrawView) this.findViewById(R.id.myDrawView4);

		//analyser = Grieg.createAnalizor(filepath); //to bêdzie coœ marcinowego co jest czymœ wiêcej ni¿ plik i do niego podpinam listenery czy coœ
		//int n = c.getChannelNumber();
		//if (n == 1){
		//  DrawView b = (DrawView) this.findViewById(R.id.myDrawView2);
		//	b.setVisibility(View.GONE);	
		//}
	}
	
	@Override
	public void onWindowFocusChanged (boolean hasFocus) {
		Log.e(ID,"Woo, windowfocus");
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus && firstFocus){
			firstFocus = false;

			Log.e(ID,"starting sending the pixels");
			secondChannelWave.setBackgroundColor(Color.RED);
			firstChannelPower.setBackgroundColor(Color.BLUE);
			secondChannelPower.setBackgroundColor(Color.MAGENTA);
			int w = firstChannelWave.getWidth();
			Log.e(ID,"width: "+w);
			int h = firstChannelWave.getHeight();
			Log.e(ID,"height: "+h);
			
			//WaveCompressor wc = new WaveCompressor(w,h);
			//analyser.addCompressor(wc);
			//analyser.addWaveListener(this);
			
			Random r = new Random();
			for(int i=0;i<w;i++){
				firstChannelWave.addNextPixel(r.nextFloat()*(h)-h/2,r.nextFloat()*(h)-h/2);
				firstChannelPower.addNextPixel(r.nextFloat()*(h)-h/2,r.nextFloat()*(h)-h/2);
			}
			w = secondChannelWave.getWidth();
			h= secondChannelWave.getHeight();
			for(int i=0;i<w;i++){
				secondChannelWave.addNextPixel(r.nextFloat()*(h)-h/2,r.nextFloat()*(h)-h/2);
				secondChannelPower.addNextPixel(r.nextFloat()*(h)-h/2,r.nextFloat()*(h)-h/2);
			}
			
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void getPixelForWave(float min, float max, int channel) {
		//DrawView firstChannelWave = (DrawView) this.findViewById(R.id.myDrawView1);
		//DrawView secondChannelWave = (DrawView) this.findViewById(R.id.myDrawView2);
		switch(channel){
		case 1: 
			firstChannelWave.addNextPixel(min,max); break;
		case 2: 
			secondChannelWave.addNextPixel(min, max); break;
		default: 
			Log.e(ID,"Illegal channel number"); break;
		}
		
	}

	@Override
	public void getPixelForPower(float min, float max, int channel) {
		//DrawView firstChannelWave = (DrawView) this.findViewById(R.id.myDrawView3);
		//DrawView secondChannelWave = (DrawView) this.findViewById(R.id.myDrawView4);
		switch(channel){
		case 1: 
			firstChannelPower.addNextPixel(min,max); break;
		case 2: 
			secondChannelPower.addNextPixel(min, max); break;
		default: 
			Log.e(ID,"Illegal channel number"); break;
		}
		
	}

}
