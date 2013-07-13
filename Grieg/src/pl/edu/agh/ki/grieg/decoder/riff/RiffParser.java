package pl.edu.agh.ki.grieg.decoder.riff;

import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.utils.BinaryInputStream;
import pl.edu.agh.ki.grieg.utils.Bytes;

public class RiffParser {

    private static final int ASCII_RIFF = 0x52494646;

    private BinaryInputStream stream;

    public RiffParser(InputStream stream) {
        this.stream = new BinaryInputStream(stream);
    }

    public RiffHeader readRiffHeader() throws NotRiffException, IOException {
        RiffHeader header = new RiffHeader();
        int riff = stream.readIntBE();
        if (riff != ASCII_RIFF) {
            throw new NotRiffException("Invalid main chunk ID: "
                    + Bytes.intToStringBE(riff));
        }
        header.setId(riff);
        header.setSize(stream.readUnsignedLE());
        header.setFormat(stream.readIntBE());
        return header;
    }

    public ChunkHeader readChunkHeader() throws IOException {
        ChunkHeader header = new ChunkHeader();
        header.setId(stream.readIntBE());
        header.setSize(stream.readUnsignedLE());
        return header;
    }
    
    public InputStream getStream() {
        return stream;
    }

}
