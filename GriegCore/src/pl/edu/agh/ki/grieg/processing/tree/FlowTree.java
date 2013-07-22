package pl.edu.agh.ki.grieg.processing.tree;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import pl.edu.agh.ki.grieg.utils.iteratee.Enumeratee;
import pl.edu.agh.ki.grieg.utils.iteratee.Enumerator;
import pl.edu.agh.ki.grieg.utils.iteratee.Iteratee;

import com.google.common.collect.Maps;

public class FlowTree {

    /** Root of the tree */
    private Source<?> root;

    /** name -> node */
    private final Map<String, Node> nodes = Maps.newHashMap();

    public <T> FlowTree(Enumerator<? extends T> root, Class<T> output) {
        this.root = Nodes.make(root, output);
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
            throw new RuntimeException("Specified node is not a source ("
                    + clazz + ")");
        }
    }
    
    public <T> Connect connect(Iteratee<? super T> sink, Class<T> clazz) {
        return new Connect(Nodes.make(sink, clazz));
    }

    public <S, T> Connect connect(Enumeratee<? super S, ? extends T> transform,
            Class<S> input, Class<T> output) {
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

        public <T> Connect connect(Iteratee<? super T> sink, Class<T> clazz) {
            return new Connect(name, Nodes.make(sink, clazz));
        }

        public <S, T> Connect connect(
                Enumeratee<? super S, ? extends T> transform, Class<S> input,
                Class<T> output) {
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

        private FlowTree to(Source<?> source) {
            connect(sink, source);
            if (name != null) {
                nodes.put(name, sink);
            }
            return FlowTree.this;
        }

        public FlowTree toRoot() {
            return to(root);
        }

        public FlowTree to(String source) {
            return to(getSourceNode(source));
        }
    }

}
