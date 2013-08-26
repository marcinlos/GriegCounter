package pl.edu.agh.ki.grieg.util.classpath;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ClassLoaderResolverTest extends ClasspathTest {

	private ClassLoaderResolver resolver;
	
	@Before
	public void createResolver() {
		resolver = new ClassLoaderResolver(systemLoader);
	}

	@Test
	public void canLoadFileFromClasspath() {
		URL url = resolver.getResource("org.example/file");
		assertNotNull(url);
		assertEquals("file", url.getProtocol());
		assertThat(url.toString(), endsWith("org.example/file"));
	}

	@Test
	public void canLoadFileFromMultipleClasspathEntries() {
		resolver = new ClassLoaderResolver(manyLoader);
		Iterator<URL> urls = resolver.getResources("config");
		List<URL> list = Lists.newArrayList(urls);
		assertThat(list, hasSize(3));
	}

	@Test
	public void byDefaultUsesContextClassLoader() {
		Thread.currentThread().setContextClassLoader(manyLoader);
		resolver = new ClassLoaderResolver();
		URL url = resolver.getResource("unique");
		assertNotNull(url);
		assertEquals("file", url.getProtocol());
	}

	@Test
	public void canLoadDirectory() {
		URL url = resolver.getResource("org.example/");
		File file = new File(url.getPath());
		assertTrue(file.isDirectory());
		assertThat(file.toString(), containsString("org.example"));
	}

	@Test
	public void canLoadWholeClasspath() {
		Iterator<URL> roots = resolver.getResources("");
		List<URL> classpath = Lists.newArrayList(roots);
		boolean found = false;
		for (URL url : classpath) {
			File rootFile = new File(url.getFile());
			File thisTest = new File(rootFile, "pl/edu/agh/ki/grieg/util/"
					+ "classpath/ClassLoaderResolverTest.class");
			if (thisTest.exists() && thisTest.isFile()) {
				found = true;
				break;
			}
		}
		assertTrue(found);
	}
	
}
