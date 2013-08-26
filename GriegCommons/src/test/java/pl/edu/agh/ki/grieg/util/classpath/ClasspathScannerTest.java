package pl.edu.agh.ki.grieg.util.classpath;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
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

	@Before
	public void createAll() {
		scanner = new ClasspathScanner(new ClassLoaderResolver());
		URL[] urls = { Resources.get("example.jar") };
		pureJar = new URLClassLoader(urls, systemLoader.getParent());
		withJar = new URLClassLoader(urls, systemLoader);
		manyWithJar = new URLClassLoader(urls, manyLoader);
	}

	@Test
	public void returnsEmptySetForNonexistantResource() throws IOException {
		assertThat(scanner.getFiles("fef3f"), empty());
	}

	@Test
	public void returnsEmptySetForEmptyDirectoryFromFs() throws IOException {
		assertThat(scanner.getFiles("org.example/empty/"), empty());
	}

	@Test
	public void returnsContentOfFlatDirectoryFromFs() throws IOException {
		Set<String> names = scanner.getFiles("org.example/second/");
		assertEquals(ImmutableSet.of("org.example/second/config"), names);
	}
	
	@Test
	public void returnsContentOfFlatDirectoryFromJar() throws IOException {
		scanner = new ClasspathScanner(new ClassLoaderResolver(pureJar));
		Set<String> names = scanner.getFiles("pl.edu.agh.ki.grieg/");
		ImmutableSet<String> expected = ImmutableSet.<String> builder()
				.add("pl.edu.agh.ki.grieg/config.xml")
				.add("pl.edu.agh.ki.grieg/log4j.properties")
				.build();
		assertEquals(expected, names);
	}

	@Test
	public void canFindInMultipleFsEntries() throws IOException {
		scanner = new ClasspathScanner(new ClassLoaderResolver(manyWithJar));
		Set<String> names = scanner.getFiles("nested/");
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
