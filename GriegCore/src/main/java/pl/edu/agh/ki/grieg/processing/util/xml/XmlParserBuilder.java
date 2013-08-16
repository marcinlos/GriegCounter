package pl.edu.agh.ki.grieg.processing.util.xml;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.xml.sax.EntityResolver;

import pl.edu.agh.ki.grieg.processing.util.Resources;

import com.google.common.collect.Lists;

/**
 * Auxilary class provided as a way to configure and create {@link XmlParser}.
 * 
 * 
 * @author los
 */
public final class XmlParserBuilder {

    /**
     * Whether or not the parser is supposed to validate content of the
     * processed documents
     */
    private boolean validating = true;

    /** List of system identifiers dotychczas napotkany */
    private final List<String> schemas = Lists.newArrayList();

    /** Entity resolver to be used by the produced neightour */
    private EntityResolver entityResolver = null;

    /** Whether to user {@code schemaLocation} tags from the XML files */
    private boolean useSchemaLocation = false;

    /**
     * Adds specified schema locations to the set of schemas used by the
     * produced parser. Schemas are specified as the local paths in the
     * filesystem.
     * 
     * @param files
     *            Files to be added
     * @return {@code this}
     */
    public XmlParserBuilder useSchema(File... files) {
        for (File file : files) {
            schemas.add(fromFile(file));
        }
        return this;
    }

    /**
     * Adds specified schema locations to the set of schemas used by the
     * produced parser. Schemas are specified by their system identifiers - see
     * <a href="http://www.w3.org/TR/REC-xml/#dt-sysid">XML specification</a>
     * for detailed description.
     * 
     * @param systemIds
     *            System identifier of schema resources
     * @return {@code this}
     */
    public XmlParserBuilder useSchema(String... systemIds) {
        for (String systemId : systemIds) {
            schemas.add(systemId);
        }
        return this;
    }

    /**
     * Adds specified schema locations to the set of schemas used by the
     * produced parser. Schemas are specified by their URI.
     * 
     * @param uris
     *            URIs of the schema resources
     * @return {@code this}
     */
    public XmlParserBuilder useSchema(URI... uris) {
        for (URI uri : uris) {
            schemas.add(fromUri(uri));
        }
        return this;
    }

    /**
     * Adds specified schema locations to the set of schemas used by the
     * produced parser. Schemas are specified by their URL.
     * 
     * @param urls
     *            URLs of the schema resources
     * @return {@code this}
     */
    public XmlParserBuilder useSchema(URL... urls) {
        for (URL url : urls) {
            schemas.add(fromUrl(url));
        }
        return this;
    }

    /**
     * Adds specified schema locations to the set of schemas used by the
     * produced parser. Schemas are specified as the classpath-relative
     * locations.
     * 
     * <p>
     * Note: unlike other types of resources, classpath-relative paths are
     * resolved immediately during the {@link #useClasspathSchema} call, hence
     * the exception declaration. If any of the paths is invalid, all the other
     * locations are discarded as well.
     * 
     * @param paths
     *            Locations of schema resources
     * @return {@code this}
     * @throws XmlSchemaNotFoundException
     *             If there is no resource the specified location
     */
    public XmlParserBuilder useClasspathSchema(String... paths)
            throws XmlSchemaNotFoundException {
        for (String path : paths) {
            schemas.add(fromClasspath(path));
        }
        return this;
    }

    /**
     * Instructs the parser-in-spe to use {@code schemaLocation} tags, if
     * present in the root of parsed documents. By default they are discarded.
     * 
     * @return {@code this}
     */
    public XmlParserBuilder useSchemaLocationHints() {
        useSchemaLocation = true;
        return this;
    }

    /**
     * Specifies an entity resolver to be used by the parser under construction.
     * 
     * @param resolver
     *            Entity resolver
     * @return {@code this}
     */
    public XmlParserBuilder withResolver(EntityResolver resolver) {
        this.entityResolver = checkNotNull(resolver);
        return this;
    }

    /**
     * Specifies the entity resolver that understands addresses with
     * {@code classpath} schema part.
     * 
     * @return {@code this}
     */
    public XmlParserBuilder withClassPathAwareResolver() {
        return withResolver(ClasspathEntityResolver.INSTANCE);
    }

    /**
     * Establishes that the produced parser will not attempt to validate
     * processed documents.
     * 
     * @return {@code this}
     */
    public XmlParserBuilder doNotValidate() {
        this.validating = false;
        return this;
    }

    /**
     * Creates an {@link XmlParser} based on the configuration gathered through
     * the method calls.
     * 
     * @return New {@link XmlParser}
     * @throws XmlException
     *             If there is a problem with underlying content
     */
    public XmlParser create() throws XmlException {
        return new XmlParser(validating, systemIds(), entityResolver,
                useSchemaLocation);
    }

    private String[] systemIds() {
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
