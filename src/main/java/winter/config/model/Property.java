package winter.config.model;

import winter.config.creation.Creator;

/**
 * Properties are the primitives of Winter, and are used as the leaf-nodes of an {@link Instance} constructor hierarchy in Winter.
 *
 * @see winter.config.model
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
        BYTE {
            @Override
            public Class<?> getConstructorType() {
                return Byte.TYPE;
            }
        },
        INTEGER {
            @Override
            public Class<?> getConstructorType() {
                return Integer.TYPE;
            }
        },
        LONG {
            @Override
            public Class<?> getConstructorType() {
                return Long.TYPE;
            }
        },
        FLOAT {
            @Override
            public Class<?> getConstructorType() {
                return Float.TYPE;
            }
        },
        DOUBLE {
            @Override
            public Class<?> getConstructorType() {
                return Double.TYPE;
            }
        },
        STRING {
            @Override
            public Class<?> getConstructorType() {
                return String.class;
            }
        };

        public abstract Class<?> getConstructorType();
    }
}
