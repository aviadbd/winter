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
}
