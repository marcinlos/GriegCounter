package pl.edu.agh.ki.grieg.data;

import pl.edu.agh.ki.grieg.io.SampleEnumerator;



public interface AudioSource {

    SourceDetails getDetails();
    
    SampleEnumerator getSampleEnumerator();
    
}
