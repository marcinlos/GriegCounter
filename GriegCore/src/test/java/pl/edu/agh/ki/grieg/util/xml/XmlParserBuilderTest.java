package pl.edu.agh.ki.grieg.util.xml;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import pl.edu.agh.ki.grieg.processing.util.Resources;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlException;
import pl.edu.agh.ki.grieg.processing.util.xml.XmlParserBuilder;

public class XmlParserBuilderTest {

    private static final String PEOPLE_XSD = "xml/general/people.xsd";
    private static final String MATH_XSD = "xml/general/math.xsd";

    private static final URL PEOPLE_XSD_URL = Resources.get(PEOPLE_XSD);
    private static final URL MATH_XSD_URL = Resources.get(MATH_XSD);
    
    private static final URI PEOPLE_XSD_URI;
    private static final URI MATH_XSD_URI; 

    private static final String PEOPLE_NO_SCHEMA = 
            "xml/general/people-no-schema.xml";
    
    private static final String BOTH_NO_SCHEMA =
            "xml/general/both-no-schema.xml";
    
    static {
        try {
            PEOPLE_XSD_URI = PEOPLE_XSD_URL.toURI();
            MATH_XSD_URI = MATH_XSD_URL.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void canUseOneClasspathSchema() throws XmlException {
        new XmlParserBuilder()
            .useClasspathSchema(PEOPLE_XSD)
            .create()
            .parse(Resources.asStream(PEOPLE_NO_SCHEMA));
    }

    @Test
    public void canUseClasspathSchemas() throws XmlException {
        new XmlParserBuilder()
            .useClasspathSchema(PEOPLE_XSD)
            .useClasspathSchema(MATH_XSD)
            .create()
            .parse(Resources.asStream(BOTH_NO_SCHEMA));
    }

    @Test
    public void canUseUrlSchemas() throws XmlException {
        new XmlParserBuilder()
            .useSchema(PEOPLE_XSD_URL)
            .useSchema(MATH_XSD_URL)
            .create()
            .parse(Resources.asStream(BOTH_NO_SCHEMA));
    }
    
    @Test
    public void canUseUriSchemas() throws Exception {
        new XmlParserBuilder()
            .useSchema(PEOPLE_XSD_URL.toURI())
            .useSchema(MATH_XSD_URL.toURI())
            .create()
            .parse(Resources.asStream(BOTH_NO_SCHEMA));
    }
    
    @Test
    public void canUseSystemIdSchemas() throws XmlException {
        new XmlParserBuilder()
            .useSchema(PEOPLE_XSD_URL.toExternalForm())
            .useSchema(MATH_XSD_URL.toExternalForm())
            .create()
            .parse(Resources.asStream(BOTH_NO_SCHEMA));
    }
    
    @Test
    public void canUseFileSchemas() throws Exception {
        new XmlParserBuilder()
            .useSchema(new File(PEOPLE_XSD_URI))
            .useSchema(new File(MATH_XSD_URI))
            .create()
            .parse(Resources.asStream(BOTH_NO_SCHEMA));
    }
    
}
