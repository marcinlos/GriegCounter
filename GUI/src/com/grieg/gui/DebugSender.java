package com.grieg.gui;

import java.util.Random;
import java.util.TimerTask;

import android.util.Log;

import com.grieg.listeners.SpectrumListener;

public class DebugSender extends TimerTask {

    private final SpectrumListener view;
    private int w;
    private int h;

    public DebugSender(SpectrumListener view, int width, int height) {
        this.view = view;
        w = width;
        h = height;
    }

    @Override
    public void run() {
    	Random r = new Random();
        for(int i=0;i<w;i++){
        	view.getPixelForSpectrum(r.nextFloat()*(h)-h/2,r.nextFloat()*(h)-h/2);
		}
        Log.e("Timer","sent new pixels?");
    }
}