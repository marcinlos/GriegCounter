package pl.edu.agh.ki.grieg.util.converters;


public class ConversionSuccessTestBase<T> extends ConversionTestBase {
    
    protected final String string;
    
    protected final T value;
    
    public ConversionSuccessTestBase(String string, T value) {
        this.string = string;
        this.value = value;
    }
    
}
