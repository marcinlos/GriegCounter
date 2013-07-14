package pl.edu.agh.ki.grieg.decoder.riff;

import pl.edu.agh.ki.grieg.utils.Bytes;

public class ChunkHeader {
    
    private int id;
    private long size;
    
    public ChunkHeader() {
        // empty
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String idAsString() {
        return Bytes.intToStringBE(id);
    }
    
    public long getSize() {
        return size;
    }
    
    public void setSize(long size) {
        this.size = size;
    }
}
