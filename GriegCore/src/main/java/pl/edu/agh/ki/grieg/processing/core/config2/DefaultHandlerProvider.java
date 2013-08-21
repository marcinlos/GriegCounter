package pl.edu.agh.ki.grieg.processing.core.config2;

import java.util.Map;

import com.google.common.collect.Maps;

public class DefaultHandlerProvider implements ContentHandlerProvider {

    private final Map<String, ContentHandler<?>> handlers;

    public DefaultHandlerProvider(Map<String, ContentHandler<?>> handlers) {
        this.handlers = handlers;
    }

    public DefaultHandlerProvider() {
        this(Maps.<String, ContentHandler<?>> newHashMap());
    }

    public void register(String qualifier, ContentHandler<?> handler) {
        handlers.put(qualifier, handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentHandler<?> forQualifier(String qualifier) {
        return handlers.get(qualifier);
    }

}
