package model;

import creation.CreationException;
import creation.Creator;

/**
 * Compounds in Winter serve as a bridge for the Many-to-Many relationship between {@link model.Instance} objects.
 *
 * @see model
 */
public final class Compound implements Parameter {

    private final Instance linkedInstance;
    private final Class<?> constructorType;

    public Compound(Class<?> constructorType, Instance linkedInstance) {
        this.linkedInstance = linkedInstance;
        this.constructorType = constructorType;
    }

    public Instance getLinkedInstance() {
        return linkedInstance;
    }

    @Override
    public Class<?> getConstructorType() {
        return constructorType;
    }

    @Override
    public Object getValue(Creator creator) throws CreationException {
        return creator.create(getLinkedInstance());
    }
}
