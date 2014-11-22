package model;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PropertyTest {

    @Test
    public void getConstructorType_int() {
        Property property = new Property(5);

        Assert.assertEquals(property.getConstructorType(), Integer.TYPE);
    }

    @Test
    public void getConstructorType_string() {
        Property property = new Property("yo");

        Assert.assertEquals(property.getConstructorType(), String.class);
    }

    @Test
    public void toString_int() {
        Property property = new Property(5);

        Assert.assertEquals(property.toString(), "[int:5]");
    }

    @Test
    public void toString_string() {
        Property property = new Property("yo");

        Assert.assertEquals(property.toString(), "[class java.lang.String:yo]");
    }
}