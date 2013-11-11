package pl.edu.agh.ki.grieg.processing.model;

import java.util.List;

import pl.edu.agh.ki.grieg.model.CompositeModel;
import pl.edu.agh.ki.grieg.model.Model;
import pl.edu.agh.ki.grieg.model.Models;
import pl.edu.agh.ki.grieg.model.SimpleModel;

import com.google.common.collect.ImmutableList;

public class AbstractChannelModel<T> {

    protected final SimpleModel<T> leftSerie;
    protected final SimpleModel<T> rightSerie;

    protected final List<SimpleModel<T>> series;

    protected final CompositeModel<?> model;

    {
        model = Models.container();
    }
    
    public AbstractChannelModel(Class<? extends T> clazz) {
        leftSerie = Models.simple(clazz);
        rightSerie = Models.simple(clazz);

        series = ImmutableList.of(leftSerie, rightSerie);
        fillMainModel();
    }
    
    public AbstractChannelModel(T left, T right) {
        leftSerie = Models.simple(left);
        rightSerie = Models.simple(right);
        
        series = ImmutableList.of(leftSerie, rightSerie);
        fillMainModel();
    }
    

    private void fillMainModel() {
        model.addModel("left", leftSerie);
        model.addModel("right", rightSerie);
    }

    public Model<?> getModel() {
        return model;
    }
    
    protected void update() {
        for (SimpleModel<T> serie : series) {
            serie.update();
        }
    }
    
}
