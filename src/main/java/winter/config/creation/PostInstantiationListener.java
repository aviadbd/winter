package winter.config.creation;

import winter.config.model.Instance;

/**
 * Listener class, {@link #didCreateInstance(winter.config.model.Instance, java.lang.Object)} called immediately
 * after an object is initialized by the {@link winter.config.creation.Creator} class.
 *
 * @author aviad
 */
public interface PostInstantiationListener {
    void didCreateInstance(Instance<?> instance, Object object);
}
