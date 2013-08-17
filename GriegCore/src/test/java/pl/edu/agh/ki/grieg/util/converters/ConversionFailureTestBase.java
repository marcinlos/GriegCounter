package pl.edu.agh.ki.grieg.util.converters;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class ConversionFailureTestBase extends ConversionTestBase {

    public static List<Object[]> prepare(String[] data) {
        final List<String> strings = Arrays.asList(data);
        return Lists.transform(strings, new Function<String, Object[]>() {
            @Override
            public Object[] apply(String input) {
                return new Object[]{input};
            }
        });
    }
    
}
