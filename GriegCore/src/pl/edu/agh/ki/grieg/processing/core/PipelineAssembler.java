package pl.edu.agh.ki.grieg.processing.core;

import pl.edu.agh.ki.grieg.processing.pipeline.Pipeline;
import pl.edu.agh.ki.grieg.util.Properties;

/**
 * Interface of a pipeline factory/modifier.
 * 
 * @author los
 */
public interface PipelineAssembler {

    /**
     * Creates/modifies instance of the pipeline
     * 
     * @param pipeline
     *            Pipeline to be modified
     * @param config
     *            Configuration properties
     * @param audio
     *            Information about the audio file collected so far
     */
    void build(Pipeline<float[][]> pipeline, Properties config, Properties audio);

}
