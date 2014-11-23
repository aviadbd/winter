package creation;

import model.Instance;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Manages the {@link creation.PostInstantiationListener} and {@link creation.PreInstantiationListener}
 * items for a {@link creation.Creator} class.
 *
 * @author aviad
 */
final class CreatorListenersManager {
    private final Collection<PreInstantiationListener> preInstantiationListeners;
    private final Collection<PostInstantiationListener> postInstantiationListeners;

    CreatorListenersManager() {
        preInstantiationListeners = new HashSet<PreInstantiationListener>();
        postInstantiationListeners = new HashSet<PostInstantiationListener>();
    }

    public boolean addPreInstantiationListener(PreInstantiationListener listener) {
        return preInstantiationListeners.add(listener);
    }

    public boolean removePreInstantiationListener(PreInstantiationListener listener) {
        return preInstantiationListeners.remove(listener);
    }

    public boolean addPostInstantiationListener(PostInstantiationListener listener) {
        return postInstantiationListeners.add(listener);
    }

    public boolean removePostInstantiationListener(PostInstantiationListener listener) {
        return postInstantiationListeners.remove(listener);
    }

    public void firePreInstantiationListeners(Instance<?> instance) throws CreationException {
        for (PreInstantiationListener listener : preInstantiationListeners) {
            listener.willCreateInstance(instance);
        }
    }

    public void firePostInstantiationListeners(Instance<?> instance, Object object) {
        for (PostInstantiationListener listener : postInstantiationListeners) {
            listener.didCreateInstance(instance, object);
        }
    }
}
