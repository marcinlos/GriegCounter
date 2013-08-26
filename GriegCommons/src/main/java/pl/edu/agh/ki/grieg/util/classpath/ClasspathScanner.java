package pl.edu.agh.ki.grieg.util.classpath;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

public class ClasspathScanner {

	private final ResourceResolver resolver;

	public ClasspathScanner(ResourceResolver resolver) {
		this.resolver = Preconditions.checkNotNull(resolver);
	}

	public Set<String> getFiles(String basedir) throws IOException {
		Set<String> files = Sets.newLinkedHashSet();
		Iterator<URL> urls = resolver.getResources(basedir);
		while (urls.hasNext()) {
			URL url = urls.next();
			System.out.println(url);
			String protocol = url.getProtocol();
			if (protocol.equals("file")) {
				handleFile(basedir, files, url);
			} else if (protocol.equals("jar")) {
				JarURLConnection connection = (JarURLConnection) url
						.openConnection();
				JarFile jar = connection.getJarFile();
				Enumeration<JarEntry> entries = jar.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					String name = entry.getName();
					if (name.startsWith(basedir)) {
						String tail = name.substring(basedir.length());
						if (tail.indexOf('/') == -1) {
							if (!entry.isDirectory()) {
								files.add(name);
							}
						}
					}
				}
			} else {
				System.out.println("Unknown protocol: " + url);
			}
		}
		System.out.println("- - - - - - -- - -- - - ");
		return files;
	}

	private void handleFile(String basedir, Set<String> files, URL url) {
		File dir = new File(url.getPath());
		for (File child : dir.listFiles()) {
			if (child.isFile()) {
				files.add(basedir + child.getName());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		ClassLoaderResolver resolver = new ClassLoaderResolver();
		Iterator<URL> it = resolver.getResources("");
		while (it.hasNext()) {
			URL url = it.next();
			System.out.println(url);
			String schema = url.getProtocol();
			if ("file".equals(schema)) {
				File file = new File(url.getPath());
				System.out.println("File: " + file);
			} else if ("jar".equals(schema)) {
				String specific = url.getFile();
				String[] parts = specific.split("!");
				URL location = new URL(parts[0]);
				System.out.println("JAR at: " + location);
				JarURLConnection connection = (JarURLConnection) url
						.openConnection();
				JarFile file = connection.getJarFile();
				Enumeration<JarEntry> entries = file.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					if (entry.getName().startsWith("")) {
						System.out.println(entry.getName());
					}
				}
			}
		}
	}

}
