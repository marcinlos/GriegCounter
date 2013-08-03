package pl.edu.agh.ki.grieg.chart;

import java.util.Map;


public interface ChartModel<T> {
    
    void add(String name, Serie<T> serie);
    
    void remove(Serie<T> serie);
    
    void remove(String name);
    
    void removeAll(String... names);
    
    void removeAll(Iterable<String> names);

    Serie<T> getSerie(String name);
    
    boolean hasSeries(String name);
    
    Map<String, Serie<T>> getSeries();

    void addListener(ChartListener<T> listener);
    
    void removeListener(ChartListener<T> listener);
    
    void addListener(String name, SerieListener<T> listener);
    
    void removeListener(String name, SerieListener<T> listener);

}