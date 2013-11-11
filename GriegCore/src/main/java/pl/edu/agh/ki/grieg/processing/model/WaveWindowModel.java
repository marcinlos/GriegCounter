package pl.edu.agh.ki.grieg.processing.model;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.io.AudioFile;
import pl.edu.agh.ki.grieg.io.SampleEnumerator;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.processing.core.ProcessingListener;
import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.Reflection;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;
import pl.edu.agh.ki.grieg.util.math.Point;
import pl.edu.agh.ki.grieg.util.properties.Properties;

public class WaveWindowModel extends AbstractChannelModel<List<Point>>
        implements Iteratee<float[][]>, ProcessingListener {

    private final LinkedList<Point> leftQueue = Lists.newLinkedList();
    private final LinkedList<Point> rightQueue = Lists.newLinkedList();

    private final int size;

    public WaveWindowModel(int size) {
        super(Reflection.<List<Point>> castClass(List.class));
        leftSerie.update(leftQueue);
        rightSerie.update(rightQueue);

        this.size = size;
    }

    @Override
    public State step(float[][] item) {

        for (int i = 0; i < item.length; ++i) {
            Model<List<Point>> serie = series.get(i);
            LinkedList<Point> data = (LinkedList<Point>) serie.getData();

            synchronized (data) {
                int toAdd = item[i].length;
                if (toAdd > size) {
                    toAdd = size;
                }
                int toDrop = data.size() - size + toAdd;
                for (int j = 0; j < toDrop; ++j) {
                    data.removeFirst();
                }
                if (toDrop > 0) {
                    int n = data.size();
                    for (int j = 0; j < n; ++j) {
                        Point p = data.removeFirst();
                        float x = j / (float) size;
                        data.addLast(new Point(x, p.y));
                    }
                }
                int base = data.size();
                for (int j = 0; j < toAdd; ++j) {
                    int num = base + j;
                    float x = num / (float) size;
                    data.add(new Point(x, item[i][j]));
                }
            }
        }
        update();

        return State.Cont;
    }
    

    private void reset() {
        for (Model<List<Point>> serie : series) {
            List<Point> data = serie.getData();
            synchronized (data) {
                data.clear();
            }
        }
        update();
    }

    @Override
    public void finished() {
        reset();
    }

    @Override
    public void failed(Throwable e) {
        reset();
    }

    @Override
    public void fileOpened(AudioFile file, Properties config) {
        // empty
    }

    @Override
    public void beforePreAnalysis(ExtractionContext ctx) {
        // empty
    }

    @Override
    public void afterPreAnalysis(Properties results) {
        // empty
    }

    @Override
    public void beforeAnalysis(Pipeline<float[][]> pipeline,
            SampleEnumerator source) {
        pipeline.connect(this, float[][].class).toRoot();
    }

    @Override
    public void afterAnalysis() {
        // empty
    }

}
