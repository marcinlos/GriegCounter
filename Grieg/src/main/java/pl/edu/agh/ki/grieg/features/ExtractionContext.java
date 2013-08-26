package pl.edu.agh.ki.grieg.features;

import java.util.Collections;
import java.util.Set;

import pl.edu.agh.ki.grieg.util.ProgressListener;
import pl.edu.agh.ki.grieg.util.properties.Key;
import pl.edu.agh.ki.grieg.util.properties.Properties;
import pl.edu.agh.ki.grieg.util.properties.PropertyMap;

import com.google.common.collect.Sets;

/**
 * Class encapsulating all the state and data associated with generic feature
 * extraction process.
 *  
 * @author los
 */
public class ExtractionContext {

    /** Features that need to be extracted */
    private final Set<String> requested;

    /** Features extracted so far */
    private final Properties features;
    
    /** Generic extraction configuration properties */
    private final Properties config;
    
    /** Extraction process progress listeners */
    private final Set<ProgressListener> progressListeners;
    
    /** Feature listeners, notified after feature extraction */
    private final Set<FeaturesListener> featureListeners;
    
    {
        requested = Sets.newHashSet();
        features = new PropertyMap();
        
        progressListeners = Sets.newCopyOnWriteArraySet();
        featureListeners = Sets.newCopyOnWriteArraySet();
    }
    
    public ExtractionContext(Properties config) {
        this.config = new PropertyMap(config);
    }
    
    public ExtractionContext() {
        this(new PropertyMap());
    }
    
    public void requestFeature(String... features) {
        for (String feature : features) {
            requested.add(feature);
        }
    }
    
    public void requestFeatures(Key<?>... features) {
        for (Key<?> feature : features) {
            requested.add(feature.name);
        }
    }
    
    public boolean isFeatureNeeded(String feature) {
        return requested.contains(feature);
    }
    
    public boolean isFeatureNeeded(Key<?> feature) {
        return requested.contains(feature.name);
    }
    
    public boolean shouldCompute(String feature) {
        return requested.contains(feature) && hasFeature(feature);
    }
    
    public boolean shouldCompute(Key<?> feature) {
        String name = feature.name;
        return requested.contains(name) && ! hasFeature(feature);
    }
    
    public Set<String> getRequestedFeatures() {
        return Collections.unmodifiableSet(requested);
    }
    
    public Set<String> getComputedFeatures() {
        return Collections.unmodifiableSet(features.keySet());
    }
    
    public Set<String> getMissingFeatures() {
        Set<String> missing = Sets.newHashSet(requested);
        missing.removeAll(features.keySet());
        return missing;
    }

    public void addProgressListener(ProgressListener listener) {
        progressListeners.add(listener);
    }
    
    public void addFeaturesListener(FeaturesListener listener) {
        featureListeners.add(listener);
    }
    
    public boolean hasProperty(String name) {
        return config.contains(name);
    }
    
    public boolean hasProperty(Key<?> key) {
        return config.contains(key);
    }
    
    public <T> T getProperty(String name, Class<T> type) {
        return config.get(name, type);
    }
    
    public <T> T getProperty(Key<T> key) {
        return config.get(key);
    }
    
    public <T> void setProperty(String name, T value) {
        config.put(name, value);
    }
    
    public <T> void setProperty(Key<T> key, T value) {
        config.put(key, value);
    }
    
    public Properties getConfig() {
        return config;
    }
    
    public boolean hasFeature(String feature) {
        return features.contains(feature);
    }
    
    public boolean hasFeature(Key<?> feature) {
        return features.contains(feature);
    }
    
    public <T> T getFeature(String name, Class<T> type) {
        return features.get(name, type);
    }
    
    public <T> T getFeature(Key<T> key) {
        return features.get(key);
    }
    
    public <T> void setFeature(String name, T value) {
        features.put(name, value);
        signalFeature(name, value);
    }
    
    public <T> void setFeature(Key<T> key, T value) {
        features.put(key, value);
        signalFeature(key.name, value);
    }
    
    public Properties getFeatures() {
        return features;
    }
    
    public void signalFeature(String name, Object value) {
        for (FeaturesListener listener : featureListeners) {
            listener.extracted(name, value);
        }
    }
    
    public void signalStart() {
        for (ProgressListener listener : progressListeners) {
            listener.started();
        }
    }
    
    public void signalProgress(float progress) {
        for (ProgressListener listener : progressListeners) {
            listener.progress(progress);
        }
    }
    
    public void signalFinish() {
        for (ProgressListener listener : progressListeners) {
            listener.finished();
        }
    }
    
    public void signalFailure(Exception e) {
        for (ProgressListener listener : progressListeners) {
            listener.failed(e);
        }
    }
    
}
