package pl.edu.agh.ki.grieg.decoder.ogg;

import static java.lang.Math.min;

import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioStream;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;

public class OggStream implements AudioStream {

    private static final int BUFFER_SIZE = 4096;

    private final SyncState syncState = new SyncState();
    private final StreamState streamState = new StreamState();
    private final Page page = new Page();
    private final Packet packet = new Packet();

    private final Info info = new Info();
    private final Comment comment = new Comment();
    private final DspState decoder = new DspState();
    private final Block block = new Block(decoder);

    private final InputStream input;
    private final SoundFormat format;

    private final float[][][] sampleBuffer = new float[1][][];
    private final int[] offsets;

    private final int convsize;
    private int sampleCount;
    private int consumed;

    public OggStream(InputStream input) throws IOException, DecodeException {
        this.input = input;

        syncState.init();
        readFirstPage();
        format = new SoundFormat(info.rate, info.channels);
        offsets = new int[info.channels];
        convsize = BUFFER_SIZE / info.channels;

        readTwoInitialHeaders();
    }

    private boolean decodeNextPacket() throws DecodeException, IOException {
        if (readPacket()) {
            if (block.synthesis(packet) == 0) {
                decoder.synthesis_blockin(block);
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean consumePCM() {
        int count = decoder.synthesis_pcmout(sampleBuffer, offsets);
        if (count == 0) {
            return false;
        }
        consumed = 0;
        sampleCount = min(count, convsize);
        decoder.synthesis_read(sampleCount);
        return true;
    }

    private boolean hasSomeDecoded() {
        return consumed < sampleCount;
    }

    @Override
    public int readSamples(float[][] buffer) throws AudioException, IOException {
        if (!hasSomeDecoded()) {
            while (!consumePCM()) {
                if (!decodeNextPacket()) {
                    return 0;
                }
            }
        }
        int bufferSize = buffer[0].length;
        int remaining = sampleCount - consumed;
        int count = min(remaining, bufferSize);

        float[][] samples = sampleBuffer[0];
        for (int i = 0; i < info.channels; ++i) {
            int offset = consumed + offsets[i];
            System.arraycopy(samples[i], offset, buffer[i], 0, count);
        }
        consumed += count;
        return count;
    }

    private void readTwoInitialHeaders() throws DecodeException, IOException {
        for (int i = 0; i < 2; ++i) {
            if (readPacket()) {
                info.synthesis_headerin(comment, packet);
            } else {
                throw new DecodeException("End of data before end of header");
            }
        }
        decoder.synthesis_init(info);
        block.init(decoder);
    }

    private boolean readPacket() throws DecodeException, IOException {
        while (true) {
            int res = streamState.packetout(packet);
            if (res > 0) {
                return true;
            } else if (res < 0) {
                throw new DecodeException("Error while forming packet");
            }
            if (!readPage()) {
                return false;
            }
        }
    }

    private boolean readPage() throws DecodeException, IOException {
        if (page.eos() != 0) {
            return false;
        }
        while (true) {
            int res = syncState.pageout(page);
            if (res > 0) {
                streamState.pagein(page);
                return true;
            } else if (res < 0) {
                throw new DecodeException("Error while forming page");
            }
            if (readSome() == 0) {
                return false;
            }
        }
    }

    private void readFirstPage() throws IOException, DecodeException {
        readSome();
        if (syncState.pageout(page) != 1) {
            throw new DecodeException("Probably not a vorbis bitstream");
        }
        streamState.init(page.serialno());

        info.init();
        comment.init();

        if (streamState.pagein(page) < 0) {
            throw new DecodeException("Error while consuming first page");
        }
        if (streamState.packetout(packet) != 1) {
            throw new DecodeException("Error while forming the first packet");
        }
        if (info.synthesis_headerin(comment, packet) < 0) {
            throw new DecodeException("Error while reading header");
        }
    }

    private int readSome() throws IOException {
        int index = syncState.buffer(BUFFER_SIZE);
        byte[] buffer = syncState.data;
        int bytes = input.read(buffer, index, BUFFER_SIZE);
        syncState.wrote(bytes);
        return bytes;
    }

    @Override
    public void close() throws IOException {
        input.close();
    }

    @Override
    public SoundFormat getFormat() {
        return format;
    }

}
