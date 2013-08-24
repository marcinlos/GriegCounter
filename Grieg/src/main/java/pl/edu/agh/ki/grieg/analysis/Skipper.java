package pl.edu.agh.ki.grieg.analysis;

import pl.edu.agh.ki.grieg.util.PeriodicTask;
import pl.edu.agh.ki.grieg.util.iteratee.AbstractEnumeratee;
import pl.edu.agh.ki.grieg.util.iteratee.State;

public class Skipper extends AbstractEnumeratee<float[][], float[]> {
            
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

}