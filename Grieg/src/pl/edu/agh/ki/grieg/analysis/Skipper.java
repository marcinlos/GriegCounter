package pl.edu.agh.ki.grieg.analysis;

import pl.edu.agh.ki.grieg.utils.PeriodicTask;
import pl.edu.agh.ki.grieg.utils.iteratee.AbstractEnumerator;
import pl.edu.agh.ki.grieg.utils.iteratee.Enumeratee;
import pl.edu.agh.ki.grieg.utils.iteratee.State;

public class Skipper extends AbstractEnumerator<float[]> implements
        Enumeratee<float[][], float[]> {
            
    private float[][] currentData;
    
    private int total;
    
    private final PeriodicTask counter;

    public Skipper(int gapSize) {
        this.counter = new PeriodicTask(gapSize) {
            @Override
            protected void execute() {
                int i = getTotal() - total - 1;
                float[] data = new float[currentData.length];
                for (int j = 0; j < data.length; ++ j) {
                    data[j] = currentData[j][i];
                }
                pushChunk(data);
            }
        };
    }

    @Override
    public State step(float[][] item) {
        final int length = item[0].length;
        currentData = item;
        counter.step(length);
        total += length;
        return State.Cont;
    }

    @Override
    public void finished() {
        // empty
    }

    @Override
    public void failed(Throwable e) {
        // empty
    }

}