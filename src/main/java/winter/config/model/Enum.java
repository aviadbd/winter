package winter.config.model;

import winter.config.creation.CreationException;
import winter.config.creation.Creator;

/**
 * Created by Tzachi on 24/11/2014.
 */
public class Enum implements Parameter {

    private final Object value;

    public Enum(Object value) {
        this.value = value;
    }

    @Override
    public Class<?> getConstructorType() {
        return value.getClass();
    }

    @Override
    public Object getValue(Creator creator) throws CreationException {
        return value;
    }

    @Override
    public String toString() {
        return String.format("[%s:%s]", getConstructorType(), value);
    }

}
