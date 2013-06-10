package griegcounter.audio;

import java.io.File;

public class TrackLoader {
    
    private TrackLoader() {
        // empty
    }
    
    private static class AsyncLoader implements Runnable {
        
        private LoadCallback cb;
        private File file;
        
        public AsyncLoader(File file, LoadCallback cb) {
            this.file = file;
            this.cb = cb;
        }
        
        @Override
        public void run() {
            try {
                cb.completed(new Track(file));
            } catch (Throwable e) {
                cb.failed(e);
            }
        }
    }
    
    public static Track load(File file) {
        return new Track(file);
    }
    
    public static Track load(String path) {
        return new Track(new File(path));
    }
    
    public static void loadAsync(File file, LoadCallback cb) {
        new Thread(new AsyncLoader(file, cb)).start();
    }
    
    public static void loadAsync(String path, LoadCallback cb) {
        loadAsync(new File(path), cb);
    }
    
}
