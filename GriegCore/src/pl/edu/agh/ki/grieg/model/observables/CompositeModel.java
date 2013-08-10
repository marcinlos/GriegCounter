package pl.edu.agh.ki.grieg.model.observables;

import java.util.Map;

public class CompositeModel<T> extends AbstractModel<T> {

    public CompositeModel(Class<? extends T> dataType) {
        super(dataType);
    }

    @Override
    public <S> void addListener(String path, Listener<? super S> listener,
            Class<S> clazz) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeListener(String path, Listener<?> listener) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean hasChild(String path) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Model<?> getChild(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Model<?>> getChildren() {
        // TODO Auto-generated method stub
        return null;
    }


}
