package winter.config.model;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PropertyTest {

    @Test
    public void getConstructorType_byte() {
        Property property = new Property(5, Property.PrimitiveType.BYTE);
        Assert.assertEquals(property.getConstructorType(), Byte.TYPE);
    }

    @Test
    public void getConstructorType_int() {
        Property property = new Property(5, Property.PrimitiveType.INTEGER);
        Assert.assertEquals(property.getConstructorType(), Integer.TYPE);
    }

    @Test
    public void getConstructorType_long() {
        Property property = new Property(5, Property.PrimitiveType.LONG);
        Assert.assertEquals(property.getConstructorType(), Long.TYPE);
    }

    @Test
    public void getConstructorType_float() {
        Property property = new Property(4.2, Property.PrimitiveType.FLOAT);
        Assert.assertEquals(property.getConstructorType(), Float.TYPE);
    }

    @Test
    public void getConstructorType_double() {
        Property property = new Property(4.2, Property.PrimitiveType.DOUBLE);
        Assert.assertEquals(property.getConstructorType(), Double.TYPE);
    }

    @Test
    public void getConstructorType_string() {
        Property property = new Property("yo", Property.PrimitiveType.STRING);
        Assert.assertEquals(property.getConstructorType(), String.class);
    }

    @Test
    public void getConstructorType_enum() {
        Parameter property = new Enum(E.E1);
        Assert.assertEquals(property.getConstructorType(), E.class);
    }


    @Test
    public void toString_int() {
        Property property = new Property(5, Property.PrimitiveType.INTEGER);
        Assert.assertEquals(property.toString(), "[int:5]");
    }

    @Test
    public void toString_long() {
        Property property = new Property(5, Property.PrimitiveType.LONG);
        Assert.assertEquals(property.toString(), "[long:5]");
    }

    @Test
    public void toString_string() {
        Property property = new Property("yo", Property.PrimitiveType.STRING);
        Assert.assertEquals(property.toString(), "[class java.lang.String:yo]");
    }

    @Test
    public void toString_enum() {
        Parameter property = new Enum(E.E1);
        Assert.assertEquals(property.toString(), "[class winter.config.model.PropertyTest$E:E1]");
    }

    enum E{
        E1;
    }
}