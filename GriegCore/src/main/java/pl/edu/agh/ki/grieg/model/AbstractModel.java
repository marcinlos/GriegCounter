package pl.edu.agh.ki.grieg.model;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Abstract partial implementation of the {@link Model} interface.
 * 
 * @author los
 * 
 * @param <T>
 *            Type of the underlying data contained by the model
 */
abstract class AbstractModel<T> implements Model<T> {

    /** Type of the model data */
    private final Class<? extends T> dataType;

    /** Actual model data */
    private volatile T data;

    /** Model listeners */
    private final Set<Listener<? super T>> listeners;

    /**
     * Initializes {@link AbstractModel} with the specified data type.
     * 
     * @param dataType
     *            Type of the model data
     */
    public AbstractModel(Class<? extends T> dataType) {
        this.dataType = dataType;
        listeners = Sets.newCopyOnWriteArraySet();
    }

    /**
     * Initializes {@link AbstractModel} with the specified initial value.
     * 
     * @param data
     *            Non-{@code null} Initial value of the model data
     */
    @SuppressWarnings("unchecked")
    public AbstractModel(T data) {
        this((Class<? extends T>) data.getClass());
        this.data = data;
    }

    /**
     * Checks if the {@code produced} type can be used as the {@code consumed}
     * value. If not, throws {@link InvalidModelTypeException}.
     */
    private static void checkTypesMatch(Class<?> consumed, Class<?> produced) {
        if (!consumed.isAssignableFrom(produced)) {
            throw new InvalidModelTypeException(consumed, produced);
        }
    }

    /**
     * Checks if the {@code produced} type can be used as the {@code consumed}
     * value. If not, throws {@link InvalidModelTypeException}. Otherwise,
     * returns the listener casted to appropriate type.
     */
    @SuppressWarnings("unchecked")
    private static <T> Listener<T> checkTypes(Listener<?> listener,
            Class<?> consumed, Class<? extends T> produced) {
        checkTypesMatch(consumed, produced);
        return (Listener<T>) listener;
    }

    /**
     * Cheks if the specified model's data can be used as {@code consumed}
     * value. If not, throws {@link InvalidModelNameException}. Otherwise,
     * returns model downcasted to the desired type.
     */
    @SuppressWarnings("unchecked")
    private static <T> Model<T> checkTypes(Model<?> model,
            Class<? extends T> consumed) {
        Class<?> produced = model.getDataType();
        checkTypesMatch(consumed, produced);
        return (Model<T>) model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(Listener<? super T> listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(Listener<?> listener) {
        listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S> void addListener(Path path, Listener<? super S> listener,
            Class<S> clazz) {
        if (path.isEmpty()) {
            addListener(checkTypes(listener, clazz, dataType));
        } else {
            Model<S> child = getChild(path, clazz);
            if (child != null) {
                child.addListener(listener);
            } else {
                throw new NoSuchModelException();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(Path path, Listener<?> listener) {
        if (path.isEmpty()) {
            removeListener(listener);
        } else {
            Model<?> child = getChild(path);
            if (child != null) {
                child.removeListener(listener);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void update() {
        for (Listener<? super T> listener : listeners) {
            listener.update(data);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void update(T newValue) {
        this.data = newValue;
        update();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S> void addListener(String path, Listener<? super S> listener,
            Class<S> clazz) {
        addListener(new Path(path), listener, clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(String path, Listener<?> listener) {
        removeListener(new Path(path), listener);
    }

    /**
     * Retrieves the direct child of the specified name, or {@code null} if
     * there is none.
     */
    private Model<?> getDirectChild(String name) {
        return getChildren().get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getData() {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S> S getData(Class<S> dataType) {
        if (data == null || dataType.isInstance(data)) {
            return dataType.cast(data);
        } else {
            throw new InvalidModelTypeException(dataType, data.getClass());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends T> getDataType() {
        return dataType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasChild(Path path) {
        return getChild(path) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasChild(String path) {
        return hasChild(new Path(path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S> Model<S> getChild(Path path, Class<? extends S> type) {
        Model<?> model = getChild(path);
        return model == null ? null : checkTypes(model, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S> Model<S> getChild(String path, Class<? extends S> type) {
        return getChild(new Path(path), type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Model<?> getChild(Path path) {
        if (path.isEmpty()) {
            return this;
        } else {
            String head = path.head();
            Path tail = path.tail();
            Model<?> child = getDirectChild(head);
            return child == null ? null : child.getChild(tail);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Model<?> getChild(String path) {
        return getChild(new Path(path));
    }

}
