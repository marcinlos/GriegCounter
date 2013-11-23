package pl.edu.agh.ki.grieg.android.demo;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import pl.edu.agh.ki.grieg.model.Listener;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.util.SpectrumBinsCalculator;
import pl.edu.agh.ki.grieg.util.math.Point;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class SpectrumView extends View implements Listener<float[]> {

	
	private float[] data;
	private final Paint paint;
	private int viewWidth;
	private int viewHeight;
	
	private double minFreq = 20;
    private double samplingFrequency = 44100;

    private final int barCount = 60;
    
    private double min = -40;
    private double max = 60;

	public SpectrumView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.paint = makePaint();
		setBackgroundColor(Color.BLACK);
		// TODO Auto-generated constructor stub
	}

	private Paint makePaint() {
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setColor(Color.GREEN);
		return p;
	}

	@Override
	public void update(float[] data) {
		postInvalidate();
	}
	
	public void setData(float[] data){
		checkNotNull(data);
		this.data = data.clone();
		invalidate();
	}
	
	public void setModel(Model<float[]> model) {
        model.addListener(this);
        //setData(model.getData());
    }
	
	@Override
    public void onSizeChanged(int w, int h, int ow, int oh) {
		super.onSizeChanged(w,h,ow,oh);
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data != null) {
            synchronized (data) {
                drawLines(canvas);
            }
        }
    }
    
    private void drawLines(Canvas canvas) {
    	SpectrumBinsCalculator calc = new SpectrumBinsCalculator(barCount,
                minFreq);
    	double[] bars = calc.compute(data, samplingFrequency);
    	double xscale = viewWidth / (double) barCount;
    	
    	for (int i = 0; i < barCount; ++i) {
            int xpos = (int) (i * xscale);
            int xnext = (int) ((i + 1) * xscale);
            int width = xnext - xpos - 1;

            double y = (bars[i] - min) / (max - min);
            int ypos = (int) ((1 - y) * viewHeight);
            canvas.drawRect(xpos, viewHeight - ypos-1, xpos+width-1, ypos, paint);
            //graphics.fillRect(xpos, ypos, width, viewHeight - ypos);
        }
    	
    }

}
