package winter.config.creation;

import winter.config.model.Instance;

/**
 * Listener class, {@link #willCreateInstance(winter.config.model.Instance)} called immediately
 * before an object is initialized by the {@link winter.config.creation.Creator} class.
 * Note that the {@link winter.config.model.Parameter} array retrieved by {@link winter.config.model.Instance#getParameters()}
 * can be manipulated within this class to affect the initialization process.
 *
 * @author aviad
 */
public interface PreInstantiationListener {
    void willCreateInstance(Instance<?> instance) throws CreationException;
}
