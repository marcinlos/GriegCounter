package pl.edu.agh.ki.grieg.model.observables;

import java.util.Collections;
import java.util.Map;

/**
 * Simple model, leaf in the model tree. Contains the value and has no
 * submodels. 
 * 
 * @author los
 * 
 * @param <T>
 */
public class SimpleModel<T> extends AbstractModel<T> {

    public SimpleModel(Class<? extends T> dataType) {
        super(dataType);
    }
    
    public static <T> SimpleModel<T> of(Class<? extends T> dataType) {
        return new SimpleModel<T>(dataType);
    }

    @Override
    public Map<String, Model<?>> getChildren() {
        return Collections.emptyMap();
    }

}
