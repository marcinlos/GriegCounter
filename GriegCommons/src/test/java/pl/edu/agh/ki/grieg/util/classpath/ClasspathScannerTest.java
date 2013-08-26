package pl.edu.agh.ki.grieg.util.classpath;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.ki.grieg.util.Resources;

import com.google.common.collect.ImmutableSet;

public class ClasspathScannerTest extends ClasspathTest {

    private ClasspathScanner scanner;
    private ClassLoader pureJar;
    private ClassLoader withJar;
    private ClassLoader manyWithJar;

    private ResourceResolver pureJarResolver;
    private ResourceResolver withJarResolver;
    private ResourceResolver manyWithJarResolver;

    private ProtocolHandlerProvider handlers;

    @Before
    public void createAll() {
        URL[] urls = { Resources.get("example.jar") };

        pureJar = new URLClassLoader(urls, systemLoader.getParent());
        withJar = new URLClassLoader(urls, systemLoader);
        manyWithJar = new URLClassLoader(urls, manyLoader);

        pureJarResolver = new ClassLoaderResolver(pureJar);
        withJarResolver = new ClassLoaderResolver(withJar);
        manyWithJarResolver = new ClassLoaderResolver(manyWithJar);

        handlers = new DefaultProtocolHandlerProvider();
        scanner = new ClasspathScanner(withJarResolver, handlers);
    }

    @Test(expected = NullPointerException.class)
    public void cannotCreateWithNullResolver() {
        new ClasspathScanner(null, handlers);
    }

    @Test(expected = NullPointerException.class)
    public void cannotCreateWithNullHandlerProvider() {
        new ClasspathScanner(withJarResolver, null);
    }

    @Test
    public void returnsEmptySetForNonexistantResource() throws Exception {
        assertThat(scanner.getEntries("org.example/fef3f"), empty());
    }

    @Test
    public void returnsEmptySetForNonexistantDirectory() throws Exception {
        assertThat(scanner.getEntries("asdfadf/"), empty());
    }

    @Test
    public void returnsEmptySetForEmptyDirectoryFromFs() throws Exception {
        assertThat(scanner.getEntries("org.example/empty/"), empty());
    }

    @Test
    public void returnsFileFromFs() throws Exception {
        Set<String> files = scanner.getEntries("META-INF/test/somefile");
        assertEquals(ImmutableSet.of("META-INF/test/somefile"), files);
    }

    @Test
    public void returnsContentOfFlatDirectoryFromFs() throws Exception {
        Set<String> names = scanner.getEntries("org.example/second/");
        assertEquals(ImmutableSet.of("org.example/second/config"), names);
    }

    @Test
    public void returnsContentOfFlatDirectoryFromJar() throws Exception {
        scanner = new ClasspathScanner(pureJarResolver, handlers);
        Set<String> names = scanner.getEntries("pl.edu.agh.ki.grieg/");
        ImmutableSet<String> expected = ImmutableSet.<String> builder()
                .add("pl.edu.agh.ki.grieg/config.xml")
                .add("pl.edu.agh.ki.grieg/log4j.properties")
                .build();
        assertEquals(expected, names);
    }

    @Test
    public void returnsEmptyWhenGivenOrdinaryFileAsDirectoryFs()
            throws Exception {
        Set<String> names = scanner.getEntries("org.example/file/");
        assertThat(names, empty());
    }

    @Test
    public void returnsEmptyWhenGivenOrdinaryFileAsDirectoryJar()
            throws Exception {
        Set<String> names = scanner.getEntries("topLevelA/");
        assertThat(names, empty());
    }

    @Test
    public void canFindInMultipleFsEntries() throws Exception {
        scanner = new ClasspathScanner(manyWithJarResolver, handlers);
        Set<String> names = scanner.getEntries("nested/");
        Set<String> expected = ImmutableSet.<String> builder()
                .add("nested/firstNestedA")
                .add("nested/firstNestedB")
                .add("nested/secondNestedA")
                .add("nested/secondNestedB")
                .add("nested/thirdNestedA")
                .add("nested/thirdNestedB")
                .add("nested/jarNestedA")
                .add("nested/jarNestedB")
                .build();
        assertEquals(expected, names);
    }

}
