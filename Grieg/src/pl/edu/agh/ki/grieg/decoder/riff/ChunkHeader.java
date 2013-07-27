package pl.edu.agh.ki.grieg.decoder.riff;

import pl.edu.agh.ki.grieg.util.Bytes;

/**
 * Header of a single chunk in the RIFF file.
 * 
 * @author los
 */
public class ChunkHeader {

    private int id;
    private long size;

    /**
     * Creates new RIFF chunk header
     */
    public ChunkHeader() {
        // empty
    }

    /**
     * @return ID of the chunk
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the chunk.
     * 
     * @param id
     *            New ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return ID of the chunk in textual format
     */
    public String idAsString() {
        return Bytes.intToStringBE(id);
    }

    /**
     * @return Size of the chunk (in bytes) minus the header size
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the size of the chunk.
     * 
     * @param size
     *            New size
     */
    public void setSize(long size) {
        this.size = size;
    }
}
