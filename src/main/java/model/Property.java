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
        Type_Integer {
            @Override
            public Class<?> getConstructorType() {
                return getConstructorTypeHelp(Integer.class);
            }
        },
        Type_Long {
            @Override
            public Class<?> getConstructorType() {
                return getConstructorTypeHelp(Long.class);
            }
        },
        Type_String {
            @Override
            public Class<?> getConstructorType() {
                return getConstructorTypeHelp(String.class);
            }
        },
        Type_Enum{
            public Class<?> getConstructorType() {
                return getConstructorTypeHelp(Enum.class);
            }
        };

        public abstract Class<?> getConstructorType();
        public Class<?> getConstructorTypeHelp(Class clz){
            try {
                return (Class<?>) clz.getDeclaredField("TYPE").get(clz);
            } catch (NoSuchFieldException e) {
                return clz;
            } catch (IllegalAccessException e) {
                return clz;
            }
        }

    }

    public Class<?> getCoffnstructorType() {
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
