package winter.config.model;

import java.lang.reflect.Constructor;

/**
 * Instances are a representation of a constructor to be called. They hold {@link winter.config.model.Parameter} objects
 * to both identify the correct constructor to call and to populate it with objects.
 *
 * @see winter.config.model
 */
public final class Instance<T> {
    private final Class<T> instantiatedClass;
    private final Parameter[] parameters;

    public Instance(Class<T> instantiatedClass, Parameter... parameters) {
        if (instantiatedClass == null) {
            throw new NullPointerException("instantiatedClass");
        }

        if (parameters == null) {
            throw new NullPointerException("parameters");
        }

        if (parameters.length == 0) {
            throw new IllegalArgumentException("parameters.length == 0");
        }

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] == null) {
                throw new NullPointerException("parameters[" + i + "]");
            }

        }

        this.instantiatedClass = instantiatedClass;
        this.parameters = parameters;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public Constructor<T> getInstantiationConstructor() {
        try {
            return instantiatedClass.getDeclaredConstructor(getConstructorTypes());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private Class<?>[] getConstructorTypes() {
        Class<?>[] constructorTypes = new Class<?>[parameters.length];

        for (int i = 0; i < getParameters().length; i++) {
            Parameter parameter = getParameters()[i];
            constructorTypes[i] = parameter.getConstructorType();
        }

        return constructorTypes;
    }
}
