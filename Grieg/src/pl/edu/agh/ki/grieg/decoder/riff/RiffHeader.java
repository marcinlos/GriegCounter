package pl.edu.agh.ki.grieg.decoder.riff;

import pl.edu.agh.ki.grieg.utils.Bytes;

public class RiffHeader extends ChunkHeader {

    private int format;
    
    public RiffHeader() {
        // empty
    }

    public int getFormat() {
        return format;
    }
    
    public String formatAsString() {
        return Bytes.intToStringBE(format);
    }

    public void setFormat(int format) {
        this.format = format;
    }

}
