package pl.edu.agh.ki.grieg.decoder;

/**
 * Thrown then attempt to parser audio file has failed because no decoder
 * supports its encoding format. 
 * 
 * @author los
 */
public class NoSuitableDecoderException extends DecodeException {

    public NoSuitableDecoderException() {
        // empty
    }

    public NoSuitableDecoderException(String message) {
        super(message);
    }

}
