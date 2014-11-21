package model;

import creation.CreationException;
import creation.Creator;

/**
 * Parameters are a way for {@link model.Instance} objects in Winter to identify the correct constructor to call
 * and to populate it with objects, using its {@link model.Parameter#getValue} method.
 *
 * @see model
 * @see creation.Creator
 */
public interface Parameter {
    Class<?> getConstructorType();

    /**
     * Returns the object represented by the parameter. Uses a {@link creation.Creator} if needed.
     * TODO: should creator be a parameter, or an interface of it?
     *
     * @param creator
     * @return
     */
    Object getValue(Creator creator) throws CreationException;
}
