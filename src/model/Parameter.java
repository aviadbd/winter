package model;

import creation.CreationException;
import creation.Creator;

/**
 * Created by aviadbendov on 11/19/14.
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
