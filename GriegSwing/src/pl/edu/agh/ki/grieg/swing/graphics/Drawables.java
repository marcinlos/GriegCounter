package pl.edu.agh.ki.grieg.swing.graphics;

import pl.edu.agh.ki.grieg.swing.graphics.transform.Transform;
import pl.edu.agh.ki.grieg.swing.graphics.transform.Transforms;

public final class Drawables {

    private Drawables() {
        // empty
    }

    public static void setBounds(Drawable d, float xmin, float xmax,
            float ymin, float ymax) {
        float w = xmax - xmin;
        float h = ymin - ymax; // reversed!
        float sx = d.getWidth() / w;
        float sy = d.getHeight() / h;
        Transform t = Transforms.newBuilder()
                .move(-xmin, -ymax)
                .scale(sx, sy)
                .build();
        d.push(t);
    }

    /*public static void drawWave(Track track, Drawable[] channels, int count) {
        Sample s = track.getSample();
        float[] data = track.allocBuffer();
        long n = track.frames();
        int c = Math.min(track.channels(), channels.length);
        int step = (int) (n / count);

        PathDrawer[] drawers = new PathDrawer[c];
        for (int i = 0; i < c; ++i) {
            drawers[i] = new PathDrawer(channels[i]);
            setBounds(channels[i], 0, n, -1, 1);
        }

        for (int i = 0; i < n; i += step) {
            s.getFrame(i, data);
            for (int j = 0; j < c; ++j) {
                Point p = new Point(i, data[j]);
                drawers[j].put(p);
            }
        }
        popTransforms(channels);
    }

    public static void drawPower(Track track, Drawable[] channels, int count) {
        Sample s = track.getSample();
        long n = track.frames();
        int c = Math.min(track.channels(), channels.length);
        int step = (int) (n / count);
        float[][] data = new float[c][step];
        PathDrawer[] drawers = new PathDrawer[c];
        for (int i = 0; i < c; ++i) {
            drawers[i] = new PathDrawer(channels[i]);
            setBounds(channels[i], 0, n, 0, 1);
        }
        for (int i = 0; i < n; i += step) {
            double[] accum = new double[c];
            s.getFrames(i, data);
            for (int j = 0; j < c; ++ j) {
                for (float val: data[j]) {
                    accum[j] += val * val;
                }
                accum[j] /= step;
                float power = max(1 + (float) log10(sqrt(accum[j])) / 2, 0);
                Point p = new Point(i, power);
                channels[j].line(new Point(i, 0), p);
            }
        }
        popTransforms(channels);
    }*/

}
