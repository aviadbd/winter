package model;

import creation.Creator;

/**
 * Properties are the primitives of Winter, and are used as the leaf-nodes of an {@link model.Instance} constructor hierarchy in Winter.
 *
 * @see model
 */
public final class Property implements Parameter {
    private final Object value;

    public Property(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public Class<?> getConstructorType() {
        Class<?> clz = value.getClass();
        try {
            return (Class<?>) clz.getDeclaredField("TYPE").get(clz);
        } catch (NoSuchFieldException e) {
            return clz;
        } catch (IllegalAccessException e) {
            return clz;
        }
    }

    @Override
    public Object getValue(Creator creator) {
        return getValue();
    }
}
