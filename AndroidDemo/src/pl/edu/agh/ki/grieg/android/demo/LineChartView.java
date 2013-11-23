package pl.edu.agh.ki.grieg.android.demo;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import pl.edu.agh.ki.grieg.model.Listener;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.util.math.Point;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LineChartView extends View implements Listener<List<Point>> {

    private final Paint paint;
    private boolean scaleChanged = false;

    private List<Point> data;

    private float viewWidth;
    private float viewHeight;

    public LineChartView(Context context, AttributeSet attributes) {
        super(context, attributes);
        this.paint = makePaint();
        setBackgroundColor(Color.BLACK);
    }
    
    public void setZeroToOneScale(){
    	scaleChanged = true;
    }

    public void setData(List<Point> data) {
        checkNotNull(data);
        this.data = data;
        invalidate();
    }
    
    public void setModel(Model<List<Point>> model) {
        model.addListener(this);
        setData(model.getData());
    }

    private Paint makePaint() {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.GREEN);
        return p;
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

    public Point toScreen(Point p) {
    	float y = p.y;
    	if(!scaleChanged)
    		y = 0.5f * (1 - p.y);
        int screenx = (int) (p.x * viewWidth);
        int screeny = (int) (y * viewHeight);
        return new Point(screenx, screeny);
    }

    private void drawLines(Canvas canvas) {
        for (int i = 1; i < data.size(); ++i) {
            Point a = data.get(i - 1), b = data.get(i);
            Point p = toScreen(a), q = toScreen(b);
            canvas.drawLine(p.x, p.y, q.x, q.y, paint);
        }
    }

    @Override
    public void update(List<Point> serie) {
        postInvalidate();
    }
    
    

}
