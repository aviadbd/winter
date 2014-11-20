package creation;

import model.Instance;
import model.Parameter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aviadbendov on 11/19/14.
 */
public final class Creator {
    private final Map<Instance, Object> cachedInstances = new HashMap<Instance, Object>();

    public <T> T create(Instance<T> instance) throws CreationException {
        // Might be cached.
        T created = (T) cachedInstances.get(instance);

        if (created != null) {
            return created;
        }

        Parameter[] parameters = instance.getParameters();
        Object[] objects = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            objects[i] = parameter.getValue(this);
        }

        try {
            created = instance.getInstantiationConstructor().newInstance(objects);

            cachedInstances.put(instance, created);

            return created;
        } catch (InstantiationException e) {
            throw new CreationException(e);
        } catch (IllegalAccessException e) {
            throw new CreationException(e);
        } catch (InvocationTargetException e) {
            throw new CreationException(e);
        }
    }
}
