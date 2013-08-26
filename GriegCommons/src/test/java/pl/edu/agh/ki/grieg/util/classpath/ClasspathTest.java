package pl.edu.agh.ki.grieg.util.classpath;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Set;

import org.junit.After;
import org.junit.Before;

import com.google.common.collect.Sets;

public class ClasspathTest {

	protected ClassLoader systemLoader;
	protected ClassLoader manyLoader;
	protected ClassLoader contextLoader;

	public ClasspathTest() {
		super();
	}

	@Before
	public void setup() throws IOException {
		systemLoader = ClassLoader.getSystemClassLoader();
		URL[] urls = subdirs(systemLoader.getResources("org.example"));
		manyLoader = new URLClassLoader(urls, systemLoader.getParent());
		saveContextClassloader();

	}

	@Before
	public void saveContextClassloader() {
		contextLoader = Thread.currentThread().getContextClassLoader();
	}

	@After
	public void restoreContextClassloader() {
		Thread.currentThread().setContextClassLoader(contextLoader);
	}

	protected URL[] subdirs(Enumeration<URL> urls) {
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
	
	final FileFilter isDir = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			return pathname.exists() && pathname.isDirectory();
		}
	};

}