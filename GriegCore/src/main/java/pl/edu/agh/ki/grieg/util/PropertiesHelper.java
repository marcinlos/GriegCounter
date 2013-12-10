package pl.edu.agh.ki.grieg.util;

import java.util.Map.Entry;

import pl.edu.agh.ki.grieg.util.properties.Properties;

public final class PropertiesHelper {

    private PropertiesHelper() {
        // non-instantiable
    }
    
    public static String dump(Properties properties) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        if (properties.isEmpty()) {
            return "    (empty)";
        }
        for (Entry<String, ?> entry : properties.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            if (first) {
                first = false;
            } else {
                sb.append('\n');
            }
            sb.append("    ")
              .append(formatPair(key, val))
              .append(" [").append(val.getClass().getName()).append("]");
        }
        return sb.toString();
    }
    
    public static String dumpWithoutTypes(Properties properties) {
    	StringBuilder sb = new StringBuilder();
        boolean first = true;
        if (properties.isEmpty()) {
            return "";
        }
        for (Entry<String, ?> entry : properties.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            if (first) {
                first = false;
            } else {
                sb.append('\n');
            }
            sb.append("    ")
              .append(formatPair(key, val));
        }
        return sb.toString();
    }
    
    private static String formatPair(String key, Object val) {
        return String.format("%-15s -> %s", key, val);
    }

}
