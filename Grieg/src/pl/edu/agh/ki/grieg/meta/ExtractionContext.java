package pl.edu.agh.ki.grieg.meta;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import pl.edu.agh.ki.grieg.util.Key;
import pl.edu.agh.ki.grieg.util.ProgressListener;
import pl.edu.agh.ki.grieg.util.Properties;
import pl.edu.agh.ki.grieg.util.PropertyMap;

import com.google.common.collect.Sets;

/**
 * Class encapsulating all the state and data associated with generic feature
 * extraction process.
 *  
 * @author los
 */
public class ExtractionContext {

    /** File to be processed */
    private final File file;
    
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
    
    public ExtractionContext(File file) {
        this.file = file;
        requested = Sets.newHashSet();
        features = new PropertyMap();
        config = new PropertyMap();
        progressListeners = Sets.newCopyOnWriteArraySet();
        featureListeners = Sets.newCopyOnWriteArraySet();
    }
    
    public File getFile() {
        return file;
    }
    
    public void request(String... features) {
        for (String feature : features) {
            requested.add(feature);
        }
    }
    
    public void request(Key<?>... features) {
        for (Key<?> feature : features) {
            requested.add(feature.name);
        }
    }
    
    public boolean isNeeded(String feature) {
        return requested.contains(feature);
    }
    
    public boolean isNeeded(Key<?> feature) {
        return requested.contains(feature.name);
    }
    
    public boolean shouldCompute(String feature) {
        return requested.contains(feature) && hasFeature(feature);
    }
    
    public boolean shouldCompute(Key<?> feature) {
        String name = feature.name;
        return requested.contains(name) && ! hasFeature(feature);
    }
    
    public Set<String> getRequested() {
        return Collections.unmodifiableSet(requested);
    }
    
    public Set<String> getComputed() {
        return Collections.unmodifiableSet(features.keySet());
    }
    
    public Set<String> getMissing() {
        Set<String> missing = Sets.newHashSet(requested);
        missing.removeAll(features.keySet());
        return missing;
    }

    public void addProgressListener(ProgressListener listener) {
        progressListeners.add(listener);
    }
    
    public Set<ProgressListener> getProgressListeners() {
        return Collections.unmodifiableSet(progressListeners);
    }

    public void addFeaturesListener(FeaturesListener listener) {
        featureListeners.add(listener);
    }
    
    public Set<FeaturesListener> getFeaturesListeners() {
        return Collections.unmodifiableSet(featureListeners);
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
        notifyAboutFeature(name, value);
    }
    
    public <T> void setFeature(Key<T> key, T value) {
        features.put(key, value);
        notifyAboutFeature(key.name, value);
    }
    
    public Properties getFeatures() {
        return features;
    }
    
    public void notifyAboutFeature(String name, Object value) {
        for (FeaturesListener listener : featureListeners) {
            listener.extracted(name, value);
        }
    }
    
    public void notifyStarted() {
        for (ProgressListener listener : progressListeners) {
            listener.started();
        }
    }
    
    public void notifyProgress(float progress) {
        for (ProgressListener listener : progressListeners) {
            listener.progress(progress);
        }
    }
    
    public void notifyFinished() {
        for (ProgressListener listener : progressListeners) {
            listener.finished();
        }
    }
    
    public void notifyFailed(Exception e) {
        for (ProgressListener listener : progressListeners) {
            listener.failed(e);
        }
    }
    
}
