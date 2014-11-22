package model;

import creation.CreationException;
import creation.Creator;

/**
 * Represents a <code>null</code> value as a parameter, while retaining the required type for the {@link model.Instance}'s constructor.
 */
public final class Null implements Parameter {
    private final Class<?> constructorType;

    public Null(Class<?> constructorType) {
        this.constructorType = constructorType;
    }

    @Override
    public Class<?> getConstructorType() {
        return constructorType;
    }

    @Override
    public Object getValue(Creator creator) throws CreationException {
        return null;
    }
}
