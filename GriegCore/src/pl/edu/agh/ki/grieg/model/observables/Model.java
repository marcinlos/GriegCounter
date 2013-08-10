package pl.edu.agh.ki.grieg.model.observables;

import java.util.Map;

/**
 * Interface of an arbitrary value that can be observed by listeners which are
 * notified when the value is updated.
 * 
 * <p>
 * Models are organized hierarchically. Each model may have zero or more
 * children. Although tree structure is not required - e.g. model may be a
 * direct child of two different models, cyclic structores are strongly
 * discouraged (though still not strictly prohibited at the moment). Children
 * are named, to facilitate navigation. Model names are unique in the containing
 * model's scope. Names must conform to the format specified for path
 * components.Model name is not its intrinsic property, names are just keys in
 * the model's children map.
 * 
 * <p>
 * Children of a given model may be accessed by their paths. This provides for
 * concise and readable, declarative bindings:
 * 
 * <pre>
 * Model&lt;?&gt;  model = ...
 * model.addListener("data.wave.left", listener, Integer.class)
 * model.addListener("other.data", otherListener, String.class)
 * ...
 * </pre>
 * 
 * Each dot-separated component specifies one named model. E.g.
 * {@code "root.sound.wave.left"} specifies model {@code left} in the model
 * {@code wave} in the model {@code sound} in the model {@code root}:
 * 
 * <pre>
 * root
 *  |- sound
 *  .   |- wave
 *  .   .   |- left
 *      .   \- right
 * </pre>
 * 
 * @author los
 * 
 * @param <T>
 *            Type of the underlying data contained by the model
 */
public interface Model<T> {

    /**
     * Registeres new listener that will receive appropriate notifications when
     * the value is updated.
     * 
     * @param listener
     *            Listener to be registered
     */
    void addListener(Listener<? super T> listener);

    /**
     * Removes previously registered listener. It will no longer receive
     * notifications about the value updates. If the listener has not been
     * registered, this method does nothing.
     * 
     * @param listener
     *            Listener to be unregistered
     */
    void removeListener(Listener<?> listener);

    /**
     * Searches the model specified by the {@code path} in its submodels tree,
     * and if it finds one, attempts to register the listener with it. If the
     * listener has not been previously registered with the specified submodule,
     * this method does nothing but validates the path. If the specified path is
     * empty, adds the listener to this model.
     * 
     * @param path
     *            Path of the submodel
     * @param listener
     *            Listener to be registered
     * @param clazz
     *            Type of the requested submodel's data
     * @throws NoSuchModelException
     *             If there is no model with specified path
     * @throws InvalidModelTypeException
     *             If the specified class is not assignable from the class of
     *             the model specified by the path
     */
    <S> void addListener(Path path, Listener<? super S> listener,
            Class<S> clazz);

    /**
     * Searches the model specified by the {@code path} in its submodels tree,
     * and if it finds one, attempts to register the listener with it. If the
     * listener has not been previously registered with the specified submodule,
     * this method does nothing but validates the path. If the path does not
     * conform to the format described in the {@link Path} doc,
     * {@link InvalidPathFormatException} is thrown. If the specified path is
     * empty, adds the listener to this model.
     * 
     * @param path
     *            Path of the submodel
     * @param listener
     *            Listener to be registered
     * @param clazz
     *            Type of the requested submodel's data
     * @throws NoSuchModelException
     *             If there is no model with specified path
     * @throws InvalidModelTypeException
     *             If the specified class is not assignable from the class of
     *             the model specified by the path
     * @throws InvalidPathFormatException
     *             If the path does not conform to the path format
     */
    <S> void addListener(String path, Listener<? super S> listener,
            Class<S> clazz);

    /**
     * Removes the listener from the model specified by the path. If the path is
     * empty, this model is used. If there is no such model, or the listener has
     * not been registered with it, this method does nothing except validating
     * the path. If none of the above applies, the listener is unregistered from
     * the model specified by the path.
     * 
     * @param path
     *            Path specifying the submodel
     * @param listener
     *            Listener to be unregistered
     */
    void removeListener(Path path, Listener<?> listener);

    /**
     * Removes the listener from the model specified by the path. If the path is
     * empty, this model is used. If there is no such model, or the listener has
     * not been registered with it, this method does nothing except validating
     * the path. If the path does not conform to the format described in
     * {@link #addListener(Listener)}, {@link InvalidPathFormatException} is
     * thrown. If none of the above applies, the listener is unregistered from
     * the model specified by the path.
     * 
     * @param path
     *            Path specifying the submodel
     * @param listener
     *            Listener to be unregistered
     * @throws InvalidPathFormatException
     *             If the path does not conform to the specified format
     */
    void removeListener(String path, Listener<?> listener);

    /**
     * @return Data contained by the model
     */
    T getData();

    /**
     * @return The {@code Class} object denoting the type of the underlying data
     *         of this model
     */
    Class<? extends T> getDataType();

    /**
     * Checks whether the model has submodel of any type with matching path. If
     * the path is empty, returns {@code true}.
     * 
     * @param path
     *            Path to the submodel
     * @return {@code true} if the model exists, {@code false} otherwise
     */
    boolean hasChild(Path path);

    /**
     * Checks whether the model has submodel of any type with matching path. If
     * the path is empty, returns {@code true}.
     * 
     * @param path
     *            Path to the submodel
     * @return {@code true} if the model exists, {@code false} otherwise
     * @throws InvalidPathFormatException
     *             If the specified path does not conform to the path format
     */
    boolean hasChild(String path);

    /**
     * Retrieves the child specified by the path. If the path is empty, returns
     * this module, provided the types match. If this child does not exist
     * (either the child itself, or any component on the path), {@code null} is
     * returned. If the child exists, and if the specified {@code type} is
     * assignable from this child's data type, the child is returned. Otherwise,
     * {@link InvalidModelTypeException} is thrown.
     * 
     * @param path
     *            Path to the submodel
     * @param type
     *            Type of the data expected for the requested submodel
     * @return Child model specified by the path, or {@code null} if there is
     *         none
     * @throws InvalidModelTypeException
     *             If the actual type of the child's data is incompatible with
     *             the expected one
     */
    <S> Model<S> getChild(Path path, Class<? extends S> type);

    /**
     * Retrieves the child specified by the path. If the path is empty, returns
     * this module, provided the types match. If this child does not exist
     * (either the child itself, or any component on the path), {@code null} is
     * returned. If the child exists, and if the specified {@code type} is
     * assignable from this child's data type, the child is returned. Otherwise,
     * {@link InvalidModelTypeException} is thrown.
     * 
     * @param path
     *            Path to the submodel
     * @param type
     *            Type of the data expected for the requested submodel
     * @return Child model specified by the path, of {@code null} if there is
     *         none
     * @throws InvalidModelTypeException
     *             If the actual type of the child's data is incompatible with
     *             the expected one
     * @throws InvalidPathFormatException
     *             If the path does not conform to the path format
     */
    <S> Model<S> getChild(String path, Class<? extends S> type);

    /**
     * Retrieves the child specified by the path. If the path is empty, returns
     * this module. If this child does not exist (either the child itself, or
     * any component on the path), {@code null} is returned.
     * 
     * @param path
     *            Path to the submodel
     * @return Child model specified by the path, or {@code null} if there is
     *         none
     */
    Model<?> getChild(Path path);

    /**
     * Retrieves the child specified by the path. If the path is empty, returns
     * this module. If this child does not exist (either the child itself, or
     * any component on the path), {@code null} is returned.
     * 
     * @param path
     *            Path to the submodel
     * @return Child model specified by the path, or {@code null} if there is
     *         none
     * @throws InvalidPathFormatException
     *             If the specified path does not conform to the path format
     */
    Model<?> getChild(String path);

    /**
     * @return Immutable map containing all the direct children models
     */
    Map<String, Model<?>> getChildren();
}
