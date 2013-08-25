package pl.edu.agh.ki.grieg.decoder.mp3;

import java.io.InputStream;
import java.util.Iterator;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;

class FrameReader implements Iterable<Header> {

    private final Bitstream stream;

    private final Decoder decoder = new Decoder();

    public FrameReader(InputStream stream) {
        this.stream = new Bitstream(stream);
    }

    private Header readNext() throws DecodeException {
        try {
            return stream.readFrame();
        } catch (BitstreamException e) {
            throw new DecodeException(e);
        }
    }

    public SampleBuffer readSamples(Header header) throws DecoderException {
        return (SampleBuffer) decoder.decodeFrame(header, stream);
    }
    
    public void closeFrame() {
        stream.closeFrame();
    }

    @Override
    public Iterator<Header> iterator() {
        return new FrameIterator();
    }

    private final class FrameIterator implements Iterator<Header> {

        private Header nextHeader;
        private boolean running = true;

        private void readNextFrame() {
            try {
                nextHeader = readNext();
                if (nextHeader == null) {
                    running = false;
                }
            } catch (DecodeException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean hasNext() {
            if (nextHeader == null && running) {
                readNextFrame();
            }
            return running;
        }

        @Override
        public Header next() {
            Header header = nextHeader;
            nextHeader = null;
            return header;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
