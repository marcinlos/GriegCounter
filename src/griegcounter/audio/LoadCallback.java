package griegcounter.audio;

public interface LoadCallback {
    
    void completed(Track track);
    
    void failed(Throwable e);

}
