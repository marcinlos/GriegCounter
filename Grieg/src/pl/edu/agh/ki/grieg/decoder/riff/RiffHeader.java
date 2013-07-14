package pl.edu.agh.ki.grieg.decoder.riff;

import pl.edu.agh.ki.grieg.utils.Bytes;

/**
 * Header of the first (root) chunk of the RIFF file. Contains the file format
 * ID in addition to data contained in every chunk header.
 * 
 * @author los
 */
public class RiffHeader extends ChunkHeader {

    private int format;

    /**
     * Creates new RIFF file header
     */
    public RiffHeader() {
        // empty
    }

    /**
     * @return File format ID
     */
    public int getFormat() {
        return format;
    }

    /**
     * @return File format ID in a textual form
     */
    public String formatAsString() {
        return Bytes.intToStringBE(format);
    }

    /**
     * Sets the file format ID
     * 
     * @param format
     *            New format
     */
    public void setFormat(int format) {
        this.format = format;
    }

}
