package pl.edu.agh.ki.grieg.model.observables;

import java.util.Collections;
import java.util.Map;

public class SimpleModel<T> extends AbstractModel<T> {
    
    public SimpleModel(Class<? extends T> dataType) {
        super(dataType);
    }

    @Override
    public boolean hasChild(String path) {
        return false;
    }

    @Override
    public <S> void addListener(String path, Listener<? super S> listener,
            Class<S> clazz) {
        throw new UnsupportedOperationException("SimpleModel has no children");
    }

    @Override
    public void removeListener(String path, Listener<?> listener) {
        throw new UnsupportedOperationException("SimpleModel has no children");
    }

    @Override
    public <S> Model<? extends S> getChild(String path, Class<S> type) {
        return null;
    }
    
    @Override
    public Model<?> getChild(String path) {
        return null;
    }

    @Override
    public Map<String, Model<?>> getChildren() {
        return Collections.emptyMap();
    }

}
