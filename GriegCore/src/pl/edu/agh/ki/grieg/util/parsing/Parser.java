package pl.edu.agh.ki.grieg.util.parsing;


public interface Parser<T> {
    
    T parse(String value) throws ParseException;
    
}
