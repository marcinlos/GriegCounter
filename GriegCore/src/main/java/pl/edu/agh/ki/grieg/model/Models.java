package pl.edu.agh.ki.grieg.model;

/**
 * Helper class consisting of static utility methods for dealing with models.
 * 
 * @author los
 */
public final class Models {

    private Models() {
        // non-instantiable
    }

    /**
     * Static helper factory, creates new simple model for the specified data
     * type.
     * 
     * @param dataType
     *            Type of the data of the model
     * @return New {@link SimpleModel}
     */
    public static <T> SimpleModel<T> simple(Class<? extends T> dataType) {
        return new SimpleModel<T>(dataType);
    }

    /**
     * Static helper factory, creates new simple model for the specified data
     * type with specified initial value.
     * 
     * @param data
     *            Initial value of the data
     * @return New {@link SimpleModel}
     */
    public static <T> SimpleModel<T> simple(T data) {
        return new SimpleModel<T>(data);
    }

    /**
     * Static helper factory, creates new composite model for the specified data
     * type.
     * 
     * @param dataType
     *            Type of the data of the model
     * @return New {@link CompositeModel}
     */
    public static <T> CompositeModel<T> composite(Class<? extends T> dataType) {
        return new CompositeModel<T>(dataType);
    }

    /**
     * Static helper factory, creates new composite model for the specified data
     * type with specified initial value.
     * 
     * @param data
     *            Initial value of the data
     * @return New {@link CompositeModel}
     */
    public static <T> CompositeModel<T> composite(T data) {
        return new CompositeModel<T>(data);
    }

    /**
     * Static helper factory, creates new composite model with {@link Void} data
     * type.
     * 
     * @return New {@link CompositeModel}
     */
    public static CompositeModel<?> container() {
        return composite(Void.class);
    }

}
