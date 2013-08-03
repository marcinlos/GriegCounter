package pl.edu.agh.ki.grieg.chart;


public interface ChartListener<T> {
    
    void serieAdded(Serie<T> serie);
    
    void serieRemoved(Serie<T> serie);

}
