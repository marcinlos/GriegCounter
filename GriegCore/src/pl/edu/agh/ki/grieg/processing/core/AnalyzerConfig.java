package pl.edu.agh.ki.grieg.processing.core;

import pl.edu.agh.ki.grieg.core.FileLoader;
import pl.edu.agh.ki.grieg.util.Properties;

class AnalyzerConfig {

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

    public void setPipelineFactory(PipelineAssembler assembler) {
        this.assembler = assembler;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
