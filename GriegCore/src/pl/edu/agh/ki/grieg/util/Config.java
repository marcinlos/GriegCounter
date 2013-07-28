package pl.edu.agh.ki.grieg.util;

import java.util.Map;

import pl.edu.agh.ki.grieg.util.parsing.ParserFactory;
import pl.edu.agh.ki.grieg.util.parsing.Parsers;

import com.google.common.collect.Maps;

public class Config {

    private final Map<String, String> map;
    
    private final ParserFactory factory;
    
    public Config() {
        map = Maps.newHashMap();
        factory = Parsers.getFactory();
    }
    
    public Config(Map<String, String> map) {
        this.map = Maps.newHashMap(map);
        this.factory = Parsers.getFactory();
    }
    
}
