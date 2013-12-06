package pl.edu.agh.ki.grieg.android.demo;

import pl.edu.agh.ki.grieg.android.misc.ApkResourceResolver;
import pl.edu.agh.ki.grieg.io.FileLoader;
import pl.edu.agh.ki.grieg.processing.core.DefaultBootstrap;
import pl.edu.agh.ki.grieg.processing.core.OfflinePipelineAssebler;
import pl.edu.agh.ki.grieg.util.classpath.ClasspathScanner;
import pl.edu.agh.ki.grieg.util.classpath.DefaultProtocolHandlerProvider;
import pl.edu.agh.ki.grieg.util.classpath.ProtocolHandlerProvider;
import pl.edu.agh.ki.grieg.util.classpath.ResourceResolver;

public class SimpleAndroidBootstrap extends DefaultBootstrap {

	
	 /**
     * {@inheritDoc}
     */
    @Override
    protected void prepareFileLoader() {
        logger().info("Using file loader with .apk aware resource resolver");
        ResourceResolver resolver = new ApkResourceResolver();
        ProtocolHandlerProvider handlers = new DefaultProtocolHandlerProvider();
        ClasspathScanner scanner = new ClasspathScanner(resolver, handlers);
        FileLoader loader = new FileLoader(scanner);
        setLoader(loader);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void preparePipelineAssembler() {
        logger().info("Using offline pipeline assembler");
        setPipelineAssembler(new OfflinePipelineAssebler());
    }
}
