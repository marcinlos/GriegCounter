package pl.edu.agh.ki.grieg.decoder.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;

import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.features.AudioFeatures;
import pl.edu.agh.ki.grieg.features.ExtractionContext;
import pl.edu.agh.ki.grieg.util.converters.ConversionException;
import pl.edu.agh.ki.grieg.util.converters.ConverterMap;
import pl.edu.agh.ki.grieg.util.properties.Key;

public class JAudioTaggerMetaExtractor {

    private static final ConverterMap converters = ConverterMap.newMap();

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

            extractFromHeader(header);

            long fileSize = file.length();
            ctx.setFeature(AudioFeatures.FILE_SIZE, fileSize);

            if (tag != null) {
                addField(AudioFeatures.AUTHOR, FieldKey.ARTIST, tag);
                addField(AudioFeatures.TITLE, FieldKey.TITLE, tag);
                addField(AudioFeatures.ALBUM, FieldKey.ALBUM, tag);
                addField(AudioFeatures.GENRE, FieldKey.GENRE, tag);
                addField(AudioFeatures.TRACK, FieldKey.TRACK, tag);
                addField(AudioFeatures.TRACK_TOTAL, FieldKey.TRACK_TOTAL, tag);
                addField(AudioFeatures.DISC, FieldKey.DISC_NO, tag);
                addField(AudioFeatures.DISC_TOTAL, FieldKey.DISC_TOTAL, tag);
                addField(AudioFeatures.YEAR, FieldKey.YEAR, tag);
                extractRest(tag);
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
        } catch (ConversionException e) {
            throw new DecodeException(e);
        }
    }

    private <T> void addField(Key<T> key, FieldKey field, Tag tag)
            throws ConversionException {
        if (tag.hasField(field) && !ctx.hasFeature(key)) {
            String strValue = tag.getFirst(field);
            T value = converters.convert(strValue, key.getType());
            ctx.setFeature(key, value);
        }
    }

    private void extractFromHeader(AudioHeader header) {
        float trackLength = header.getTrackLength();
        ctx.setFeature(AudioFeatures.DURATION, trackLength);

        int sampleRate = header.getSampleRateAsNumber();
        long approxSamples = (int) (trackLength * sampleRate);
        ctx.setFeature(AudioFeatures.APPROX_SAMPLES, approxSamples);

        int bitRate = (int) header.getBitRateAsNumber();
        ctx.setFeature(AudioFeatures.BITRATE, bitRate);

        boolean vbr = header.isVariableBitRate();
        ctx.setFeature(AudioFeatures.VBR, vbr);

        String encoding = header.getEncodingType();
        ctx.setFeature(AudioFeatures.ENCODING, encoding);
    }

    private void extractRest(Tag tag) throws UnsupportedEncodingException {
        for (Iterator<TagField> it = tag.getFields(); it.hasNext();) {
            TagField field = it.next();
            if (field instanceof TagTextField) {
                TagTextField textField = (TagTextField) field;
                String key = "jtg_" + textField.getId();
                String value = textField.getContent();
                ctx.setFeature(key, value);
            }
        }
    }

    public static void process(File file, ExtractionContext context)
            throws DecodeException, IOException {
        new JAudioTaggerMetaExtractor(file, context).run();

    }

    public static void processSignalExceptions(File file,
            ExtractionContext context) {
        try {
            new JAudioTaggerMetaExtractor(file, context).run();
        } catch (IOException e) {
            context.failure(e);
        } catch (DecodeException e) {
            context.failure(e);
        }
    }

}
