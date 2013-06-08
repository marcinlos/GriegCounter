package pl.edu.agh.ki.grieg.meta;

import java.util.Map.Entry;
import java.util.Set;

/**
 * {@code TagSet} provides methods for tag values retrieval. 
 * 
 * <p>
 * TODO: Pretty much a stub for now.
 * 
 * @author los
 */
public interface TagSet {

    String get(String tagId);
    
    byte[] getImage(String tagId);
    
    Set<String> tags();
    
    Set<Entry<String, String>> entries();
    
    Set<Entry<String, byte[]>> images();
    
}
