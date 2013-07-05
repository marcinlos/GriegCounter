package pl.edu.agh.ki.grieg.example;

import java.io.File;
import java.io.IOException;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.decoder.DecodeException;
import pl.edu.agh.ki.grieg.io.AudioFile;

public class Example {

    public static void main(String[] args) throws DecodeException, IOException {
        AudioFile file = FileLoader.getInstance().loadFile(new File("/home/los/Downloads/Schubert - Serenade.wav"));
    }

}
