/**
 * This package contains API and implementation of general-purpose string
 * conversion framework.
 * 
 * <p>
 * Simplest building block and functionality provider is {@link Converter}
 * interface. The
 * {@link Converter#convert(String, com.google.common.reflect.TypeToken)} method
 * performs the actual work of converting string to the value of arbitrary type.
 * While the type system does not enforce this, return value of such call should
 * be compatible with the desired type specified by the second argument.
 * 
 * <p>
 * Type converters are in principle simple function objects, stateless
 * functionality providers. Lifetime and usage of registered {@link Converter}s
 * is unspecified. One type converter may provide conversions to many type,
 * though it seems impractical to create one God-converter. It is more
 * convenient and clear to use fine-grained converters and assemble useful
 * general converters with an appropriate container described further.
 * 
 * <p>
 * As simple, granular {@link Converter}s are of little use, they need to be
 * managed. {@link ConverterMap} is a {@link Converter} implementation that
 * aggregates and manages other converters. It uses a filter-based approach:
 * each converter is registered with a {@link com.google.common.base.Predicate}
 * determining types supported by this converter. For each conversion, converter
 * supporting requested type is searched. If there are multiple appropriate
 * converters, it is an ambiguity and an exception is thrown. There is no notion
 * of specialization, shadowing etc, each type must be supported by precisely
 * one converter.
 * 
 * @author los
 */
package pl.edu.agh.ki.grieg.util.converters;

