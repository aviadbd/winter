package model;

/**
 * Created by aviadbendov on 11/19/14.
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
}
