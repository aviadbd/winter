package model;

import creation.CreationException;
import creation.Creator;

/**
 * Created by aviadbendov on 11/19/14.
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
