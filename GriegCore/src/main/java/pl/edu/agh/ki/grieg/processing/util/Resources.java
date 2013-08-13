package pl.edu.agh.ki.grieg.processing.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.net.URL;

public final class Resources {
    
    public static final String CLASSPATH_URI_SCHEME = "classpath"; 

    private Resources() {
        // non-instantiable
    }
    

    public static ClassLoader contextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static URL get(String path) {
        checkNotNull(path);
        return contextClassLoader().getResource(path);
    }
    
    public static InputStream asStream(String path) {
        checkNotNull(path);
        return contextClassLoader().getResourceAsStream(path);
    }

}
