package creation;

import model.Instance;
import model.Parameter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by aviadbendov on 11/19/14.
 */
public final class Creator {
    private final Map<Instance, Object> cachedInstances = new HashMap<Instance, Object>();
    private final CreatorListenersManager listeners = new CreatorListenersManager();

    private final Stack<Instance<?>> instantiationStack = new Stack<Instance<?>>();

    public <T> T create(Instance<T> instance) throws CreationException {
        instantiationStack.push(instance);

        // Might be cached.
        T created = (T) cachedInstances.get(instance);

        if (created != null) {
            return created;
        }

        try {
            Constructor<T> constructor = instance.getInstantiationConstructor();

            if (constructor == null) {
                throw new CreationException("No available constructor for parameters: " + Arrays.toString(instance.getParameters()), instantiationStack);
            }

            listeners.firePreInstantiationListeners(instance);
            created = constructor.newInstance(getObjects(instance));
            listeners.firePostInstantiationListeners(instance, created);

            cachedInstances.put(instance, created);

            return created;
        } catch (InstantiationException e) {
            throw new CreationException(e, instantiationStack);
        } catch (IllegalAccessException e) {
            throw new CreationException(e, instantiationStack);
        } catch (InvocationTargetException e) {
            throw new CreationException(e, instantiationStack);
        } catch (RuntimeException e) {
            throw new CreationException(e, instantiationStack);
        } finally {
            instantiationStack.pop();
        }
    }

    private Object[] getObjects(Instance<?> instance) throws CreationException {
        Parameter[] parameters = instance.getParameters();
        Object[] objects = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            objects[i] = parameter.getValue(this);
        }

        return objects;
    }

    public boolean addPreInstantiationListener(PreInstantiationListener listener) {
        return listeners.addPreInstantiationListener(listener);
    }

    public boolean addPostInstantiationListener(PostInstantiationListener listener) {
        return listeners.addPostInstantiationListener(listener);
    }

    public boolean removePostInstantiationListener(PostInstantiationListener listener) {
        return listeners.removePostInstantiationListener(listener);
    }

    public boolean removePreInstantiationListener(PreInstantiationListener listener) {
        return listeners.removePreInstantiationListener(listener);
    }
}
