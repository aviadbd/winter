package model;

import data.D;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Constructor;

public class InstanceTest {

    Parameter five = new Property(5);
    Parameter seven = new Property(7);
    Parameter name = new Property("Dave");

    @Test(expectedExceptions = NullPointerException.class)
    public void classNull_exception() {
        new Instance<D>(null, five);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void parametersNull_exception() {
        new Instance<D>(D.class, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void parametersEmptyArray_exception() {
        new Instance<D>(D.class);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void parametersFirstElementNullInArray_exception() {
        new Instance<D>(D.class, null, five);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void parametersSecondElementNullInArray_exception() {
        new Instance<D>(D.class, five, null);
    }

    @Test
    public void oneParameter_getParameters() throws Exception {
        Instance<D> instance = new Instance<D>(D.class, five);

        Assert.assertEquals(instance.getParameters().length, 1, "Parameters.length");
        Assert.assertEquals(instance.getParameters()[0], five, "Parameters[0]");
    }

    @Test
    void twoParameters_getParameters() throws Exception {
        Instance<D> instance = new Instance<D>(D.class, five, seven);

        Assert.assertEquals(instance.getParameters().length, 2, "Parameters.length");
        Assert.assertEquals(instance.getParameters()[0], five, "Parameters[0]");
        Assert.assertEquals(instance.getParameters()[1], seven, "Parameters[1]");
    }

    @Test
    public void correctParameters_getConstructor_withInt() throws Exception {
        Instance<D> instance = new Instance<D>(D.class, five);

        Constructor<D> ctor = instance.getInstantiationConstructor();

        Assert.assertNotNull(ctor, "ctor");
        Class<?>[] parameterTypes = ctor.getParameterTypes();
        Assert.assertEquals(parameterTypes.length, 1, "parameterTypes.length");
        Assert.assertEquals(parameterTypes[0], Integer.TYPE, "parameterTypes[0]");
    }

    @Test
    public void correctParameters_getConstructor_withString() throws Exception {
        Instance<D> instance = new Instance<D>(D.class, name);

        Constructor<D> ctor = instance.getInstantiationConstructor();

        Assert.assertNotNull(ctor, "ctor");
        Class<?>[] parameterTypes = ctor.getParameterTypes();
        Assert.assertEquals(parameterTypes.length, 1, "parameterTypes.length");
        Assert.assertEquals(parameterTypes[0], String.class, "parameterTypes[0]");
    }
}