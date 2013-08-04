package pl.edu.agh.ki.grieg.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Chart<T> implements ChartModel<T> {

    private final Set<ChartListener<T>> listeners;

    private final Map<String, Serie<T>> series;

    {
        listeners = Sets.newCopyOnWriteArraySet();
        series = Maps.newConcurrentMap();
    }
    
    public static <T> Chart<T> create() {
        return new Chart<T>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Serie<T> getSerie(String name) {
        return series.get(name);
    }

    private Serie<T> mustGetSerie(String name) {
        Serie<T> serie = getSerie(name);
        if (serie == null) {
            throw NoSuchSerieException.named(name);
        } else {
            return serie;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean hasSeries(String name) {
        return series.containsKey(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(ChartListener<T> listener) {
        listeners.add(checkNotNull(listener));
    }

    /**
     * {@inheritDoc}
     */
    public void removeListener(ChartListener<T> listener) {
        listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    public void addListener(String serieName, SerieListener<T> listener) {
        mustGetSerie(serieName).addListener(checkNotNull(listener));
    }

    /**
     * {@inheritDoc}
     */
    public void removeListener(String serieName, SerieListener<T> listener) {
        mustGetSerie(serieName).removeListener(checkNotNull(listener));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void add(String name, Serie<T> serie) {
        series.put(name, serie);
        for (ChartListener<T> listener : listeners) {
            listener.serieAdded(serie);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Serie<T>> getSeries() {
        return Collections.unmodifiableMap(series);
    }

    private boolean doRemove(Serie<T> serie) {
        return series.values().remove(serie);
    }

    private Serie<T> doRemove(String name) {
        return series.remove(name);
    }

    @Override
    public synchronized void remove(Serie<T> serie) {
        if (doRemove(serie)) {
            notifyAboutRemoved(serie);
        }
    }

    @Override
    public synchronized void remove(String name) {
        Serie<T> removed = doRemove(name);
        if (removed != null) {
            notifyAboutRemoved(removed);
        }
    }

    @Override
    public synchronized void removeAll(String... names) {
        removeAll(Arrays.asList(names));
    }

    @Override
    public void removeAll(Iterable<String> names) {
        for (String name : names) {
            remove(name);
        }
    }

    private void notifyAboutRemoved(Serie<T> serie) {
        for (ChartListener<T> listener : listeners) {
            listener.serieRemoved(serie);
        }
    }

}
