package pl.edu.agh.ki.grieg.processing.pipeline;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import pl.edu.agh.ki.grieg.util.iteratee.Enumeratee;
import pl.edu.agh.ki.grieg.util.iteratee.Enumerator;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratee;
import pl.edu.agh.ki.grieg.util.iteratee.Iteratees;
import pl.edu.agh.ki.grieg.util.iteratee.State;

import com.google.common.collect.Maps;

public class Pipeline<T> implements Iteratee<T> {

    /** Root of the tree */
    private final Transform<T, T> root;

    /** name -> node */
    private final Map<String, Node> nodes = Maps.newHashMap();

    public Pipeline(Class<T> input) {
        Enumeratee<T, T> forwarder = Iteratees.forwarder();
        this.root = Nodes.make(forwarder, input, input);
    }

    public static <T> Pipeline<T> make(Class<T> input) {
        return new Pipeline<T>(input);
    }

    /**
     * Checks in runtime whether specified sink can be connected to the output
     * of specified source (whether the types are compatible).
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

    private void connect(Sink<?> sink, Source<?> source) {
        if (compatible(source, sink)) {
            Enumerator<?> src = source.getSource();
            Iteratee<?> dst = sink.getSink();
            src.connect(universal(dst));
        }
    }

    private Source<?> getSourceNode(String name) {
        Node node = nodes.get(name);
        if (node instanceof Source<?>) {
            return (Source<?>) node;
        } else if (node == null) {
            throw new RuntimeException("Specified source does not exist");
        } else {
            String clazz = node.getClass().getName();
            throw new IllegalArgumentException("Specified node is not a "
                    + "source (" + clazz + ")");
        }
    }

    @SuppressWarnings("unchecked")
    public <S> Enumerator<S> getSource(String name, Class<? extends S> clazz) {
        Source<?> node = getSourceNode(name);
        Class<?> out = node.getOutputType();
        if (clazz.isAssignableFrom(out)) {
            return (Enumerator<S>) node.getSource();
        } else {
            String pattern = "Type mismatch: source emits %s, %s required";
            throw new IllegalArgumentException(String.format(pattern,
                    out.getName(), clazz.getName()));
        }
    }

    public <S> Connect connect(Iteratee<? super S> sink, Class<S> clazz) {
        return new Connect(Nodes.make(sink, clazz));
    }

    public <S, R> Connect connect(Enumeratee<? super S, ? extends R> transform,
            Class<S> input, Class<R> output) {
        return new Connect(Nodes.make(transform, input, output));
    }

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

        public Pipeline<T> toRoot() {
            return to(root);
        }

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