package pl.edu.agh.ki.grieg.processing.core.config;

/**
 * Interface used by configuration interpreters, representing current
 * interpreting environment.
 * 
 * @author los
 */
public interface Context {
    
//    void setAssemblerDefinition(AssemblerDefinition definition);
    
    void addError(Throwable e);

}
