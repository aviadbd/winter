package winter.config.model;

import winter.config.creation.CreationException;
import winter.config.creation.Creator;

/**
 * Compounds in Winter serve as a bridge for the Many-to-Many relationship between {@link winter.config.model.Instance} objects.
 *
 * @see winter.config.model
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

    @Override
    public String toString() {
        return "[" + getConstructorType().toString() + "]";
    }
}
