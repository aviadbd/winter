package model;

import creation.Creator;

import java.util.Locale;

/**
 * Properties are the primitives of Winter, and are used as the leaf-nodes of an {@link Instance} constructor hierarchy in Winter.
 *
 * @see model
 */
public final class Property implements Parameter {
    private final Object value;
    private final PrimitiveType primitiveType;

    public Property(Object value, PrimitiveType primitiveType) {
        this.value = value;
        this.primitiveType = primitiveType;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public Class<?> getConstructorType() {
        return primitiveType.getConstructorType();
    }

    @Override
    public Object getValue(Creator creator) {
        return getValue();
    }

    @Override
    public String toString() {
        return String.format("[%s:%s]", getConstructorType().toString(), getValue().toString());
    }

    public enum PrimitiveType {
        INTEGER {
            @Override
            public Class<?> getConstructorType() {
                return Integer.TYPE;
            }
        },
        LONG {
            @Override
            public Class<?> getConstructorType() {
                return Long.class;
            }
        },
        STRING {
            @Override
            public Class<?> getConstructorType() {
                return String.class;
            }
        },
        ENUM {
            public Class<?> getConstructorType() {
                return Enum.class;
            }
        };

        public abstract Class<?> getConstructorType();
    }
}
