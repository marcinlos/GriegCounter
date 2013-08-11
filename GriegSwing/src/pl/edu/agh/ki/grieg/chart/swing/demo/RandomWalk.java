package pl.edu.agh.ki.grieg.chart.swing.demo;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import pl.edu.agh.ki.grieg.model.model.Model;
import pl.edu.agh.ki.grieg.util.Point;

class RandomWalk implements Runnable {

    private static final float MIN = 0.0f;
    private static final float MAX = 0.5f;

    private final Model<List<Point>> serie;
    private final int count;
    private float factor;
    private final long sleep;
    private final Random random = new Random();

    public RandomWalk(Model<List<Point>> serie, int count, float factor,
            long sleep) {
        this.serie = serie;
        this.count = count;
        this.factor = factor;
        this.sleep = sleep;
    }

    protected void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(sleep);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected float x(int i) {
        return i / (float) count;
    }

    @Override
    public void run() {
        float y = (MIN + MAX) / 2;
        for (int i = 0; i < count; ++i) {
            y += step();
            y = clamp(y, MIN, MAX);
            append(new Point(x(i), y - 1));
            sleep();
        }
    }

    private float step() {
        return (float) (random.nextGaussian() * factor);
    }

    private static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    private void append(Point p) {
        List<Point> data = serie.getData();
        synchronized (data) {
            data.add(p);
            serie.update();
        }
    }
}
