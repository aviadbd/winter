package model;

import java.lang.reflect.Constructor;

/**
 * Created by aviadbendov on 11/19/14.
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

    public Class<T> getInstantiatedClass() {
        return instantiatedClass;
    }

    public Constructor<T> getInstantiationConstructor() {
        return (Constructor<T>) instantiatedClass.getDeclaredConstructors()[0];
    }
}
