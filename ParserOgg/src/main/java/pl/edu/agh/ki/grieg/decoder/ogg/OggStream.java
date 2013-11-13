package pl.edu.agh.ki.grieg.decoder.ogg;

import static java.lang.Math.min;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;

import pl.edu.agh.ki.grieg.data.SoundFormat;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.io.AudioException;
import pl.edu.agh.ki.grieg.io.AudioStream;

public class OggStream implements AudioStream {

    private static final Logger logger = LoggerFactory
            .getLogger(OggStream.class);

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
            logger.debug("Decoding packet samples");
            if (block.synthesis(packet) == 0) {
                logger.debug("Synthesis indeed");
                decoder.synthesis_blockin(block);
            } else {
                logger.debug("Ooops, something else");
            }
            logger.debug("Reading PCM data");
            // consumePCM();
            return true;
        } else {
            logger.debug("Couldn't decode any samples");
            return false;
        }
    }

    private boolean consumePCM() {
        int count = decoder.synthesis_pcmout(sampleBuffer, offsets);
        if (count == 0) {
            logger.debug("... but have none :(");
            return false;
        }
        consumed = 0;
        sampleCount = min(count, convsize);
        logger.debug("Great, we have {} new samples", sampleCount);
        decoder.synthesis_read(sampleCount);
        return true;
    }

    private boolean hasSomeDecoded() {
        return consumed < sampleCount;
    }

    @Override
    public int readSamples(float[][] buffer) throws AudioException, IOException {
        logger.debug("Reading samples");
        if (!hasSomeDecoded()) {
            logger.debug("We're empty, need to decode some");
            while (!consumePCM()) {
                if (!decodeNextPacket()) {
                    return 0;
                }
            }
        }
        int bufferSize = buffer[0].length;
        int remaining = sampleCount - consumed;
        logger.debug("We have total: {}, consumed: {}, remaining: {}",
                sampleCount, consumed, remaining);
        logger.debug("Buffer is {}", bufferSize);
        int count = min(remaining, bufferSize);

        logger.debug("We can consume {} samples", count);

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
                logger.debug("Have header {}", i);
                info.synthesis_headerin(comment, packet);
            } else {
                throw new DecodeException("End of data before end of header");
            }
        }
        logger.debug("2 initial headers read");
        decoder.synthesis_init(info);
        block.init(decoder);
    }

    private boolean readPacket() throws DecodeException, IOException {
        logger.debug("Reading packet");
        while (true) {
            int res = streamState.packetout(packet);
            if (res > 0) {
                logger.debug("Packet complete");
                return true;
            } else if (res < 0) {
                throw new DecodeException("Error while forming packet");
            }
            logger.debug("Not packet yet");
            if (!readPage()) {
                logger.debug("Could not read enough data to form packet");
                return false;
            }
        }
    }

    private boolean readPage() throws DecodeException, IOException {
        if (page.eos() != 0) {
            logger.debug("Reading page, but eos encountered");
            return false;
        }
        logger.debug("Reading page");
        while (true) {
            int res = syncState.pageout(page);
            if (res > 0) {
                streamState.pagein(page);
                logger.debug("page complete");
                return true;
            } else if (res < 0) {
                throw new DecodeException("Error while forming page");
            }
            logger.debug("No page yet");
            if (readSome() == 0) {
                logger.debug("Could not read enough data to form page");
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
        logger.debug("Done reading first page, everything ok");
    }

    private int readSome() throws IOException {
        logger.debug("Reading from file");
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
