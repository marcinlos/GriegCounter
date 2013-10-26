package pl.edu.agh.ki.grieg.processing.pipeline;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.NoSuchElementException;

import pl.edu.agh.ki.grieg.util.iteratee.Enumeratee;
import pl.edu.agh.ki.grieg.util.iteratee.Enumerator;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratees;
import pl.edu.agh.ki.grieg.util.iteratee.State;

import com.google.common.collect.Maps;

/**
 * Processing pipeline, a tree-like structure consisting of one main, "root"
 * source {@link Enumerator}, and possibly many nodes ({@link Iteratee}s and
 * {@link Enumeratee}s) connected to each other in arbitrary configurations.
 * Each node can be named, to facilitate selecting the required node. Root node
 * is pluggable - is acutally more of a slot, making the {@link Pipeline}
 * composable.
 * 
 * <p>
 * {@link Pipeline} allows building the connection structure using fluent
 * interface:
 * 
 * <pre>
 * pipeline.as(&quot;segmenter&quot;)
 *      .connect(segmenter, float[][].class, float[][].class)
 * 		.toRoot();
 * 
 * pipeline.as(&quot;compressor&quot;)
 *      .connect(compressor, float[][].class, Range[].class)
 * 		.to(&quot;segmenter&quot;);
 * </pre>
 * 
 * @author los
 * 
 * @param <T>
 *            Type of the input data
 */
public class Pipeline<T> implements Iteratee<T> {

//	private static final Logger logger = LoggerFactory
//			.getLogger(Pipeline.class);

	/** Root of the tree */
	private final Transform<T, T> root;

	/** Mapping name -> node */
	private final Map<String, Node> nodes = Maps.newHashMap();

	/**
	 * Creates new {@link Pipeline} object with specified root input type.
	 * 
	 * @param input
	 *            {@code Class} object of the input type
	 */
	public Pipeline(Class<T> input) {
		Enumeratee<T, T> forwarder = Iteratees.forwarder();
		this.root = Nodes.make(forwarder, input, input);
	}

	/**
	 * Creates new {@link Pipeline} object with the specified input type.
	 * 
	 * @param input
	 *            {@code Class} object of the input type
	 * @return {@link Pipeline} object
	 */
	public static <T> Pipeline<T> make(Class<T> input) {
		return new Pipeline<T>(input);
	}

	/**
	 * Checks in runtime whether specified sink can be connected to the output
	 * of specified source (whether the types are compatible). It is done in
	 * typesafe manner, using available type information from both nodes.
	 * 
	 * @param source
	 *            Source node
	 * @param sink
	 *            Sink node
	 * @return {@code true} if it is possible to connect {@code sink} to the
	 *         {@code source}, {@code false} otherwise
	 */
	private static boolean compatible(Source<?> source, Sink<?> sink) {
		Class<?> sourceOutput = source.getOutputType();
		Class<?> sinkInput = sink.getInputType();
		return sinkInput.isAssignableFrom(sourceOutput);
	}

	/**
	 * Casts unspecified sink to universal ({@code Object}-consuming) sink.
	 * Unsafe, but called only when it is certain it is correct.
	 * 
	 * @param sink
	 *            Sink to downcast
	 * @return Universal sink
	 */
	@SuppressWarnings("unchecked")
	private static Iteratee<Object> universal(Iteratee<?> sink) {
		return (Iteratee<Object>) sink;
	}

	/**
	 * Tries to connects input of {@code sink} to the output of {@code source}.
	 * If it is possible - types match - nodes are connected.
	 * 
	 * @param sink
	 *            Node accepting data
	 * @param source
	 *            Node generating data
	 */
	private void connect(Sink<?> sink, Source<?> source) {
		if (compatible(source, sink)) {
			Enumerator<?> src = source.getSource();
			Iteratee<?> dst = sink.getSink();
			src.connect(universal(dst));
		}
	}

	/**
	 * Retrieves node with specified name as the source node, if possible. If
	 * there is no such node, {@link NoSuchElementException} is thrown. If there
	 * is, but it is not the source node, {@link IllegalArgumentException} is
	 * thrown.
	 * 
	 * @param name
	 *            Name of the node to retrieve
	 * @return Source node with the specified name
	 */
	private Source<?> getSourceNode(String name) {
		Node node = nodes.get(name);
		if (node instanceof Source<?>) {
			return (Source<?>) node;
		} else if (node == null) {
			throw new NoSuchElementException("Specified source does not exist");
		} else {
			String clazz = node.getClass().getName();
			throw new IllegalArgumentException("Specified node is not a "
					+ "source (" + clazz + ")");
		}
	}

	/**
	 * Retrieves enumerator previously registered with the specified name, and
	 * checks whether it can be used as an enumerator of the specified type. If
	 * there is no such enumerator, {@link NoSuchElementException} is thrown. If
	 * there is, but the specified type does not match the actual one,
	 * {@link IllegalArgumentException} is thrown.
	 * 
	 * @param name
	 *            Name of the enumerator to retrieve
	 * @param clazz
	 *            Type of the values retrieved enumerator is expected to produce
	 * @return Enumerator producing values of the specified type
	 */
	@SuppressWarnings("unchecked")
	public <S> Enumerator<S> getSource(String name, Class<? extends S> clazz) {
		Source<?> node = getSourceNode(checkNotNull(name));
		Class<?> out = node.getOutputType();
		if (clazz.isAssignableFrom(out)) {
			return (Enumerator<S>) node.getSource();
		} else {
			String pattern = "Type mismatch: source emits %s, %s required";
			throw new IllegalArgumentException(String.format(pattern,
					out.getName(), clazz.getName()));
		}
	}

	/**
	 * Begins the process of connecting the specified {@link Iteratee} to some
	 * output of the pipeline. This call is the first in the chained, "fluent"
	 * invocation.
	 * 
	 * @param sink
	 *            Node to connect to pipeline
	 * @param clazz
	 *            Type of the new node's input
	 * @return Inner DSL-implementing object
	 */
	public <S> Connect connect(Iteratee<? super S> sink, Class<S> clazz) {
		return new Connect(Nodes.make(sink, clazz));
	}

	/**
	 * Begins the process of connecting the specified {@link Enumeratee} to some
	 * output of the pipeline. This call is the first in the chained, "fluent"
	 * invocation.
	 * 
	 * @param transform
	 *            Content transformer
	 * @param input
	 *            Input type of the new node
	 * @param output
	 *            Output type of the new node
	 * @return Inner DSL-implementing object
	 */
	public <S, R> Connect connect(Enumeratee<? super S, ? extends R> transform,
			Class<S> input, Class<R> output) {
		return new Connect(Nodes.make(transform, input, output));
	}

	/**
	 * Establishes the name by which the node will known to the pipeline.
	 * 
	 * @param name
	 *            Name to register node with
	 * @return {@code this}
	 */
	public Named as(String name) {
		return new Named(name);
	}

	public class Named {
		private final String name;

		private Named(String name) {
			checkNotNull(name, "Null name");
			this.name = name;
		}

		public <S> Connect connect(Iteratee<? super S> sink, Class<S> clazz) {
			return new Connect(name, Nodes.make(sink, clazz));
		}

		public <S, R> Connect connect(
				Enumeratee<? super S, ? extends R> transform, Class<S> input,
				Class<R> output) {
			return new Connect(name, Nodes.make(transform, input, output));
		}
	}

	public class Connect {

		private final String name;
		private final Sink<?> sink;

		private Connect(String name, Sink<?> sink) {
			this.name = name;
			this.sink = sink;
		}

		private Connect(Sink<?> sink) {
			this(null, sink);
		}

		private Pipeline<T> to(Source<?> source) {
			connect(sink, source);
			if (name != null) {
				nodes.put(name, sink);
			}
			return Pipeline.this;
		}

		/**
		 * Connects the node to the source
		 */
		public Pipeline<T> toRoot() {
			return to(root);
		}

		/**
		 * Connects the node to some named node
		 * 
		 * @param source
		 *            Name of the node to connect to
		 */
		public Pipeline<T> to(String source) {
			return to(getSourceNode(source));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public State step(T item) {
		return root.getSink().step(item);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finished() {
		root.getSink().finished();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void failed(Throwable e) {
		root.getSink().failed(e);
	}

}
