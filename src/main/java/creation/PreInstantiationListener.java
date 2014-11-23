package creation;

import model.Instance;

/**
 * Created by aviadbendov on 11/22/14.
 */
public interface PreInstantiationListener {
    void willCreateInstance(Instance<?> instance) throws CreationException;
}
