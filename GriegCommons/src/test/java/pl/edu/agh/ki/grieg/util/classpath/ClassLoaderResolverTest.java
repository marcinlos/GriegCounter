package pl.edu.agh.ki.grieg.util.classpath;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ClassLoaderResolverTest {

	private ClassLoaderResolver resolver;

	private ClassLoader systemLoader;
	private ClassLoader manyLoader;

	private ClassLoader contextLoader;

	private final FileFilter isDir = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			return pathname.exists() && pathname.isDirectory();
		}
	};

	@Before
	public void setup() throws IOException {
		systemLoader = ClassLoader.getSystemClassLoader();
		URL[] urls = subdirs(systemLoader.getResources("org.example"));
		manyLoader = new URLClassLoader(urls, systemLoader.getParent());
		saveContextClassloader();

		resolver = new ClassLoaderResolver(systemLoader);
	}

	@Before
	public void saveContextClassloader() {
		contextLoader = Thread.currentThread().getContextClassLoader();
	}

	@After
	public void restoreContextClassloader() {
		Thread.currentThread().setContextClassLoader(contextLoader);
	}

	private URL[] subdirs(Enumeration<URL> urls) {
		Set<URL> entries = Sets.newLinkedHashSet();
		while (urls.hasMoreElements()) {
			URL root = urls.nextElement();
			File dir = new File(root.getFile());
			if (dir.isDirectory()) {
				for (File child : dir.listFiles(isDir)) {
					try {
						entries.add(child.toURI().toURL());
					} catch (MalformedURLException e) {
						// don't care
					}
				}
			}
		}
		return entries.toArray(new URL[0]);
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
		assertThat(list, hasSize(2));
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
