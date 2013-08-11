package pl.edu.agh.ki.grieg.processing.util;

import java.io.InputStream;
import java.net.URL;

public final class Resources {

    private Resources() {
        // non-instantiable
    }
    
    public static ClassLoader contextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    
    public static URL get(String path) {
        return contextClassLoader().getResource(path);
    }
    
    public static InputStream asStream(String path) {
        return contextClassLoader().getResourceAsStream(path);
    }

}
