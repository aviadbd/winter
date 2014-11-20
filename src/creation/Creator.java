package creation;

import model.Instance;
import model.Parameter;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by aviadbendov on 11/19/14.
 */
public final class Creator {
    public <T> T create(Instance<T> instance) throws CreationException {
        Parameter[] parameters = instance.getParameters();
        Object[] objects = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            objects[i] = parameter.getValue(this);
        }

        try {
            return instance.getInstantiationConstructor().newInstance(objects);
        } catch (InstantiationException e) {
            throw new CreationException(e);
        } catch (IllegalAccessException e) {
            throw new CreationException(e);
        } catch (InvocationTargetException e) {
            throw new CreationException(e);
        }
    }
}
