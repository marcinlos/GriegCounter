package pl.edu.agh.ki.grieg.processing.util.xml;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.xml.sax.EntityResolver;

import pl.edu.agh.ki.grieg.processing.util.Resources;

import com.google.common.collect.Lists;

public class XmlParserBuilder {

    private final List<String> schemas = Lists.newArrayList();

    private EntityResolver entityResolver = null;

    private boolean useSchemaLocation = false;

    public XmlParserBuilder useSchema(File... files) {
        for (File file : files) {
            schemas.add(fromFile(file));
        }
        return this;
    }

    public XmlParserBuilder useSchema(String... systemIds) {
        for (String systemId : systemIds) {
            schemas.add(systemId);
        }
        return this;
    }

    public XmlParserBuilder useSchema(URI... uris) {
        for (URI uri : uris) {
            schemas.add(fromUri(uri));
        }
        return this;
    }

    public XmlParserBuilder useSchema(URL... urls) {
        for (URL url : urls) {
            schemas.add(fromUrl(url));
        }
        return this;
    }

    public XmlParserBuilder useClasspathSchema(String... paths)
            throws XmlSchemaNotFoundException {
        for (String path : paths) {
            schemas.add(fromClasspath(path));
        }
        return this;
    }

    public XmlParserBuilder ignoreSchemaLocationHints() {
        useSchemaLocation = false;
        return this;
    }

    public XmlParserBuilder useSchemaLocationHints() {
        useSchemaLocation = true;
        return this;
    }

    public XmlParserBuilder withResolver(EntityResolver resolver) {
        this.entityResolver = checkNotNull(resolver);
        return this;
    }

    public XmlParserBuilder withClassPathAwareResolver() {
        return withResolver(ClasspathEntityResolver.INSTANCE);
    }

    public XmlParser create() throws XmlException {
        return new XmlParser(systemIds(), entityResolver, useSchemaLocation);
    }

    public String[] systemIds() {
        String[] systemIds = new String[schemas.size()];
        return schemas.toArray(systemIds);
    }

    private String fromFile(File file) {
        return fromUri(file.toURI());
    }

    private String fromUri(URI uri) {
        return uri.toASCIIString();
    }

    private String fromUrl(URL url) {
        return url.toExternalForm();
    }

    private String fromClasspath(String path) throws XmlSchemaNotFoundException {
        URL url = Resources.get(checkNotNull(path));
        if (url != null) {
            return url.toExternalForm();
        } else {
            throw new XmlSchemaNotFoundException("classpath: " + path);
        }
    }

}
