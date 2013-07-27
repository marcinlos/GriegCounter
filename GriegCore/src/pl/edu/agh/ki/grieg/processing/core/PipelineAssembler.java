package pl.edu.agh.ki.grieg.processing.core;

import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.Properties;

public interface PipelineAssembler {

    void build(Pipeline<float[][]> pipeline, Properties config, Properties audio);

}
