package creation;

import model.Instance;

/**
 * Listener class, {@link #didCreateInstance(model.Instance, java.lang.Object)} called immediately
 * after an object is initialized by the {@link creation.Creator} class.
 *
 * @author aviad
 */
public interface PostInstantiationListener {
    void didCreateInstance(Instance<?> instance, Object object);
}
