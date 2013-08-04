package pl.edu.agh.ki.grieg.model;


public interface ChartListener<T> {
    
    void serieAdded(Serie<T> serie);
    
    void serieRemoved(Serie<T> serie);

}
