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
 *            Type of the underlying data contained by the model
 */
public class SimpleModel<T> extends AbstractModel<T> {

    /**
     * Creates a new model for the specified data type.
     * 
     * @param dataType
     *            Type of the data of the model
     */
    public SimpleModel(Class<? extends T> dataType) {
        super(dataType);
    }

    /**
     * Static helper factory, creates new model for the specified data type.
     * 
     * @param dataType
     *            Type of the data of the model
     * @return New {@link SimpleModel}
     */
    public static <T> SimpleModel<T> of(Class<? extends T> dataType) {
        return new SimpleModel<T>(dataType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Model<?>> getChildren() {
        return Collections.emptyMap();
    }

}