package winter.config.model;

import winter.config.creation.CreationException;
import winter.config.creation.Creator;

/**
 * Parameters are a way for {@link Instance} objects in Winter to identify the correct constructor to call
 * and to populate it with objects, using its {@link Parameter#getValue} method.
 *
 * @see winter.config.model
 * @see winter.config.creation.Creator
 */
public interface Parameter {
    Class<?> getConstructorType();

    /**
     * Returns the object represented by the parameter. Uses a {@link winter.config.creation.Creator} if needed.
     * TODO: should creator be a parameter, or an interface of it?
     *
     * @param creator
     * @return
     */
    Object getValue(Creator creator) throws CreationException;
}
