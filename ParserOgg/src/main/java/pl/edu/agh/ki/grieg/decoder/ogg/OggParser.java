package pl.edu.agh.ki.grieg.decoder.ogg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import pl.edu.agh.ki.grieg.decoder.AbstractAudioFormatParser;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.decoder.util.JAudioTaggerMetaExtractor;
import pl.edu.agh.ki.grieg.features.AudioFeatures;
import pl.edu.agh.ki.grieg.features.ExtractionContext;

import com.jcraft.jorbis.JOrbisException;
import com.jcraft.jorbis.VorbisFile;

public class OggParser extends AbstractAudioFormatParser {

    @Override
    public OggStream openStream(InputStream stream) throws DecodeException,
            IOException {
        return new OggStream(stream);
    }

    @Override
    public void extractFeatures(File file, ExtractionContext context)
            throws IOException, DecodeException {
        try {
            JAudioTaggerMetaExtractor.processSignalExceptions(file, context);
            VorbisFile vorbis = new VorbisFile(file.getPath());
            
            int n = 0;
            context.setFeature(AudioFeatures.SAMPLES, vorbis.pcm_total(n));
            context.setFeature(AudioFeatures.BITRATE, vorbis.bitrate(n));
            context.setFeature(AudioFeatures.DURATION, vorbis.time_total(n));
            
            OggStream stream = openStream(new FileInputStream(file));
            context.setFeature(AudioFeatures.FORMAT, stream.getFormat());
            
            stream.close();
        } catch (JOrbisException e) {
            throw new DecodeException(e);
        }
    }

}
