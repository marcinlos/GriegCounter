/**
 * This package contains implementation of custom SPI mechanism used to find
 * audio file parsers.
 * 
 * <p>
 * During initialization, file matching the classpath-relative path {@c
 * pl.edu.agh.ki.grieg/parsers} are found using specified classloader (thread
 * context classloader by default), and parsed, to obtain audio parser
 * definitions. Parser definition consists of class implementing
 * {@link pl.edu.agh.ki.grieg.decoder.spi.AudioFormatParser}, and list of 
 * supported extensions. Classes found in this way are instantiated using 
 * {@link java.lang.Class#newInstance()}.
 * 
 * @author los
 */
package pl.edu.agh.ki.grieg.decoder.discovery;

