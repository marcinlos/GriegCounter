package pl.edu.agh.ki.grieg.processing.core;

import pl.edu.agh.ki.grieg.io.FileLoader;
import pl.edu.agh.ki.grieg.util.properties.Properties;

/**
 * Simple structure containing configuration required by the
 * {@link ProcessorFactory} in order to fulfill its task. Simple POJO structure.
 * 
 * @author los
 */
class FactoryConfig {

    private FileLoader fileLoader;

    private PipelineAssembler assembler;

    private Properties properties;

    public FileLoader getFileLoader() {
        return fileLoader;
    }

    public void setFileLoader(FileLoader fileLoader) {
        this.fileLoader = fileLoader;
    }

    public PipelineAssembler getPipelineFactory() {
        return assembler;
    }

    public void setPipelineAssembler(PipelineAssembler assembler) {
        this.assembler = assembler;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
