package pl.edu.agh.ki.grieg.model.observables;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;


public class CompositeModel<T> extends AbstractModel<T> {
    
    private final Map<String, Model<?>> models = Maps.newHashMap();

    public CompositeModel(Class<? extends T> dataType) {
        super(dataType);
    }
    
    public static <T> CompositeModel<T> of(Class<? extends T> dataType) {
        return new CompositeModel<T>(dataType);
    }
    
    public void addModel(String name, Model<?> model) {
        models.put(name, model);
    }
    
    public void removeModel(String name) {
        models.remove(name);
    }

    @Override
    public Map<String, Model<?>> getChildren() {
        return Collections.unmodifiableMap(models);
    }


}
