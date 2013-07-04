package com.grieg.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DrawView extends View {
	Paint paint = new Paint();
	int middle;
	int width;
	int pixelcounter=0;
	float[] pixels;
	int batchesGot=0;
	
	private boolean shouldIDrawAlready;
	private final static String ID="Drawer";
	
	
	
	

	public DrawView(Context context) {
		super(context);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(0);
		this.setBackgroundColor(Color.GREEN);
		// TODO Auto-generated constructor stub
	}
	public DrawView(Context context, AttributeSet attrs){
		super(context,attrs);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(0);
		this.setBackgroundColor(Color.GREEN);
	}
	public DrawView(Context context, AttributeSet attrs, int defStyle){
		super(context,attrs,defStyle);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(0);
		this.setBackgroundColor(Color.GREEN);
	}
	
	public void addNextPixel(float min, float max){
		int i = pixelcounter;
		//Log.e(ID,"i:" + i + " batchesGot:" + batchesGot);
		try{
			pixels[batchesGot*4] = i; //StartX
			pixels[batchesGot*4+1] = middle+min; //StartY
			pixels[batchesGot*4+2] = i; //EndX
			pixels[batchesGot*4+3] = middle+max; //EndY
			pixelcounter++;
		}
		catch(IndexOutOfBoundsException e){
			Log.e(ID,"too many pixels added, not wide enough");
		}
		
		batchesGot++;
		Log.e(ID+this.getId(),"got pixels: " + batchesGot);
		if(batchesGot == pixels.length/4){
			Log.e(ID,"all batches here");
			shouldIDrawAlready = true;
			//this.invalidate();
			this.postInvalidate();
		}
			
		
	}
	
	@Override
    public void onDraw(Canvas canvas) {
			if(shouldIDrawAlready == false){
				Log.e(ID,"I'm SO not drawing stuff");
				return;
			}
			Log.e(ID,"I'm drawing stuff!");
			
          // for(int i=0;i<width;i++){
            	canvas.drawLines(pixels, paint);
            	Log.e(ID+this.getId(),"I drew?");
           // }
			//canvas.drawLine(0, middle, width, middle*2,paint);
			//float[] f = {(float)0, (float)middle, (float)width,(float) middle*2,50,(float) middle, (float)width, (float)0};
			//canvas.drawLines(f, paint);
    }
	
	private void rescaleH(float scale){
		Log.e(ID,"I'm rescaling");
		for(int i=1;i<pixels.length;i=i+2){
			pixels[i] = pixels[i]*scale;
		}
	}
	@Override
	protected void onSizeChanged(int width,int height, int ow, int oh){
		int oldH;
		oldH = this.middle*2;
		this.width = width;
		this.middle = height/2;
		Log.e(ID+this.getId(),"sizeChanged, height: "+height);
		if (pixels == null)
			pixels = new float[width*4];
		if (pixels.length == 0){
			Log.e(ID,"view has no width to draw");
		}
		if(oldH != 0){
			rescaleH((float)height / (float)oldH);
			this.postInvalidate();
		}
		
		
	}

}
