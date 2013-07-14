package pl.edu.agh.ki.grieg.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ObjectFormatter {

    /** Default instance */
    public static final ObjectFormatter DEFAULT = new ObjectFormatter();

    private ObjectFormatter() {
        // TODO Auto-generated constructor stub
    }

    public <T> Instance name(T name) {
        return new Instance(name);
    }

    public class Instance {

        private final Object name;
        private final String left = " {";
        private final String right = " }";
        private final String sep = ", ";
        
        private Map<String, Object> props = new LinkedHashMap<String, Object>();

        private Instance(Object name) {
            this.name = name;
        }

        public <T> Instance set(String key, T value) {
            props.put(key, value);
            return this;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append(left);
            int propertyCount = props.size();
            int i = 0;
            for (Entry<String, Object> property : props.entrySet()) {
                String key = property.getKey();
                Object value = property.getValue();
                sb.append(key).append(" = ").append(value);
                if (++i < propertyCount) {
                    sb.append(sep);
                }
            }
            sb.append(right);
            return sb.toString();
        }

    }

}
