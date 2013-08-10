package pl.edu.agh.ki.grieg.model.observables;

import java.util.Set;

import com.google.common.collect.Sets;

public abstract class AbstractModel<T> implements Model<T> {

    private final Class<? extends T> dataType;

    private T data;

    private final Set<Listener<? super T>> listeners;

    public AbstractModel(Class<? extends T> dataType) {
        this.dataType = dataType;
        listeners = Sets.newCopyOnWriteArraySet();
    }

    private static void checkTypesMatch(Class<?> consumed, Class<?> produced) {
        if (!consumed.isAssignableFrom(produced)) {
            throw new InvalidModelTypeException();
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Listener<T> checkTypes(Listener<?> listener,
            Class<?> consumed, Class<? extends T> produced) {
        checkTypesMatch(consumed, produced);
        return (Listener<T>) listener;
    }

    @SuppressWarnings("unchecked")
    private static <T> Model<T> checkTypes(Model<?> model,
            Class<? extends T> consumed) {
        Class<?> produced = model.getDataType();
        checkTypesMatch(consumed, produced);
        return (Model<T>) model;
    }

    @Override
    public void addListener(Listener<? super T> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener<?> listener) {
        listeners.remove(listener);
    }

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

    public void update() {
        updateAll();
    }

    public void update(T newValue) {
        this.data = newValue;
        update();
    }
    
    private void updateAll() {
        for (Listener<? super T> listener : listeners) {
            listener.update(data);
        }
    }

    @Override
    public <S> void addListener(String path, Listener<? super S> listener,
            Class<S> clazz) {
        addListener(new Path(path), listener, clazz);
    }

    @Override
    public void removeListener(String path, Listener<?> listener) {
        removeListener(new Path(path), listener);
    }

    private Model<?> getDirectChild(String name) {
        return getChildren().get(name);
    }
    
    @Override
    public T getData() {
        return data;
    }

    @Override
    public Class<? extends T> getDataType() {
        return dataType;
    }

    @Override
    public boolean hasChild(Path path) {
        return getChild(path) != null;
    }

    @Override
    public boolean hasChild(String path) {
        return hasChild(new Path(path));
    }

    @Override
    public <S> Model<S> getChild(Path path, Class<? extends S> type) {
        Model<?> model = getChild(path);
        return model == null ? null : checkTypes(model, type);
    }
    
    @Override
    public <S> Model<S> getChild(String path, Class<? extends S> type) {
        return getChild(new Path(path), type);
    }

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

    @Override
    public Model<?> getChild(String path) {
        return getChild(new Path(path));
    }

}
