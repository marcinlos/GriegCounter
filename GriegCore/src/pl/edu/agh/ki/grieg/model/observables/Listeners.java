package pl.edu.agh.ki.grieg.model.observables;

public final class Listeners {

    private Listeners() {
        // non-instantiable
    }

    public static <T> void updateAll(
            Iterable<? extends Listener<? super T>> listeners, T data) {
        for (Listener<? super T> listener : listeners) {
            listener.update(data);
        }
    }

}
