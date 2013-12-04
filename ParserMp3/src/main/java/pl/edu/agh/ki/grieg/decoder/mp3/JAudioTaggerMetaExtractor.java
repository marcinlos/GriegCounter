package pl.edu.agh.ki.grieg.decoder.mp3;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagField;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.features.AudioFeatures;
import pl.edu.agh.ki.grieg.features.ExtractionContext;

public class JAudioTaggerMetaExtractor {

    protected final File file;

    protected final ExtractionContext ctx;

    public JAudioTaggerMetaExtractor(File file, ExtractionContext context) {
        this.file = file;
        this.ctx = context;
    }

    public void run() throws IOException, DecodeException {
        try {
            AudioFile audio = AudioFileIO.read(file);
            AudioHeader header = audio.getAudioHeader();
            Tag tag = audio.getTag();

            float trackLength = header.getTrackLength();
            ctx.setFeature(AudioFeatures.DURATION, trackLength);

            int bitRate = (int) header.getBitRateAsNumber();
            ctx.setFeature(AudioFeatures.BITRATE, bitRate);

            long fileSize = file.length();
            ctx.setFeature(AudioFeatures.FILE_SIZE, fileSize);

            boolean vbr = header.isVariableBitRate();
            ctx.setFeature(AudioFeatures.VBR, vbr);

            String encoding = header.getEncodingType();
            ctx.setFeature("encoding", encoding);

            for (Iterator<TagField> it = tag.getFields(); it.hasNext();) {
                TagField field = it.next();
                String key = "jtg_" + field.getId();
                String value = field.toString();
                ctx.setFeature(key, value);
            }

        } catch (CannotReadException e) {
            throw new DecodeException(e);
        } catch (IOException e) {
            throw new DecodeException(e);
        } catch (TagException e) {
            throw new DecodeException(e);
        } catch (ReadOnlyFileException e) {
            throw new DecodeException(e);
        } catch (InvalidAudioFrameException e) {
            throw new DecodeException(e);
        }
    }

    public static void process(File file, ExtractionContext context)
            throws DecodeException, IOException {
        new JAudioTaggerMetaExtractor(file, context).run();
    }

}
