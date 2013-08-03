package pl.edu.agh.ki.grieg.chart;


public interface SerieListener<T> {
    
    void updated(Serie<T> serie);

}
