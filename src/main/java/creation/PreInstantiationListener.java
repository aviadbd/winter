package creation;

import model.Instance;

/**
 * Listener class, {@link #willCreateInstance(model.Instance)} called immediately
 * before an object is initialized by the {@link creation.Creator} class.
 * Note that the {@link model.Parameter} array retrieved by {@link model.Instance#getParameters()}
 * can be manipulated within this class to affect the initialization process.
 *
 * @author aviad
 */
public interface PreInstantiationListener {
    void willCreateInstance(Instance<?> instance) throws CreationException;
}
