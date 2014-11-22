package model;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PropertyTest {

    @Test
    public void getConstructorType_int() {
        Property property = new Property(5, Property.PrimitiveType.Type_Integer);
        Assert.assertEquals(property.getConstructorType(), Integer.TYPE);
    }

    @Test
    public void getConstructorType_string() {
        Property property = new Property("yo", Property.PrimitiveType.Type_String);
        Assert.assertEquals(property.getConstructorType(), String.class);
    }

    @Test
    public void getConstructorType_enum() {
        Property property = new Property(E.E1, Property.PrimitiveType.Type_Enum);
        Assert.assertEquals(property.getConstructorType(), Enum.class);
    }


    @Test
    public void toString_int() {
        Property property = new Property(5, Property.PrimitiveType.Type_Integer);
        Assert.assertEquals(property.toString(), "[int:5]");
    }

    @Test
    public void toString_string() {
        Property property = new Property("yo", Property.PrimitiveType.Type_String);
        Assert.assertEquals(property.toString(), "[class java.lang.String:yo]");
    }

    @Test
    public void toString_enum() {
        Property property = new Property(E.E1, Property.PrimitiveType.Type_Enum);
        Assert.assertEquals(property.toString(), "[class java.lang.Enum:E1]");
    }

    enum E{
        E1;
    }
}