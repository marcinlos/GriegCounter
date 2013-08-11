package pl.edu.agh.ki.grieg.model;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Composite model, consisting of its own value and arbitrary number of child
 * models. Children have arbitrary types of values, and neither the type nor the
 * actual values are required to be connected in any way. The link between
 * parent and children models is purely logical one.
 * 
 * <p>
 * The model is not considered updated when some of its children are updated, so
 * if the model's value depends on submodules' values, the link needs to be made
 * explicit, i.e. parent module should register listeners with such descendants.
 * 
 * @author los
 * 
 * @param <T>
 *            Type of the underlying data contained by the model
 */
public class CompositeModel<T> extends AbstractModel<T> {

    /** Map of child models */
    private final Map<String, Model<?>> models = Maps.newHashMap();

    /**
     * Creates a new composite model for the specified data type.
     * 
     * @param dataType
     *            Type of the data of the model
     */
    public CompositeModel(Class<? extends T> dataType) {
        super(dataType);
    }

    /**
     * Creates a new model for the data type taken from the specifie initial
     * value.
     * 
     * @param data
     *            Initial value of the data
     */
    public CompositeModel(T data) {
        super(data);
    }

    /**
     * Adds new model to the list of model's children. If the specified name is
     * already used by some other child, it is replaced with the new one.
     * 
     * @param name
     *            Name with which the the module is to be registered
     * @param model
     *            Model to be registered
     * @return Previous child of such name, or {@code null} if there was none
     */
    public Model<?> addModel(String name, Model<?> model) {
        if (!Path.isValidComponent(name)) {
            throw new InvalidModelNameException(name);
        }
        return models.put(name, model);
    }

    /**
     * Removes previously registered model from the list of this model's
     * children. If the model has not been previously registered, this method
     * does nothing.
     * 
     * @param name
     *            Name of the model to be unregistered
     */
    public void removeModel(String name) {
        models.remove(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Model<?>> getChildren() {
        return Collections.unmodifiableMap(models);
    }

}
