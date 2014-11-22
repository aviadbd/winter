package creation;

import model.Instance;

/**
 * Created by aviadbendov on 11/22/14.
 */
public interface PostInstantiationListener {
    void didCreateInstance(Instance<?> instance, Object object);
}
