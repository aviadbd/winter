package creation;

import data.A;
import data.B;
import data.C;
import data.D;
import model.Compound;
import model.Instance;
import model.Null;
import model.Property;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by aviadbendov on 11/20/14.
 */
public class CreatorTest {

    @Test
    public void createD_withInt() throws Exception {
        Creator c = new Creator();
        Instance<D> instance = new Instance<D>(D.class, new Property(5));

        D result = c.create(instance);

        Assert.assertNotNull(result, "result");
        Assert.assertEquals(result.getNumber(), 5, "result.number");
        Assert.assertEquals(result.getString(), "", "result.string");
    }

    @Test
    public void createD_withString() throws Exception {
        Creator c = new Creator();
        Instance<D> instance = new Instance<D>(D.class, new Property("name"));

        D result = c.create(instance);

        Assert.assertNotNull(result, "result");
        Assert.assertEquals(result.getNumber(), 0, "result.number");
        Assert.assertEquals(result.getString(), "name", "result.string");
    }

    @Test
    public void createA_withD_Int() throws Exception {
        Creator c = new Creator();

        Instance<D> d = new Instance<D>(D.class, new Property(5));
        Instance<A> a = new Instance<A>(A.class, new Compound(D.class, d));

        A result = c.create(a);

        Assert.assertNotNull(result, "result");
        Assert.assertNotNull(result.getD1(), "result.d1");
        Assert.assertEquals(result.getD1().getNumber(), 5, "result.d1.number");
        Assert.assertEquals(result.getD1().getString(), "", "result.d1.string");
    }

    @Test
    public void createA_withD_String() throws Exception {
        Creator c = new Creator();

        Instance<D> d = new Instance<D>(D.class, new Property("name"));
        Instance<A> a = new Instance<A>(A.class, new Compound(D.class, d));

        A result = c.create(a);

        Assert.assertNotNull(result, "result");
        Assert.assertNotNull(result.getD1(), "result.d1");
        Assert.assertEquals(result.getD1().getNumber(), 0, "result.d1.number");
        Assert.assertEquals(result.getD1().getString(), "name", "result.d1.string");
    }

    @Test
    public void createC_withA() throws Exception {
        Creator creator = new Creator();

        Instance<D> d = new Instance<D>(D.class, new Property("name"));
        Instance<A> a = new Instance<A>(A.class, new Compound(D.class, d));
        Instance<C> c = new Instance<C>(C.class, new Compound(A.class, a));

        C result = creator.create(c);

        Assert.assertNotNull(result, "result");
        Assert.assertNotNull(result.getA(), "result.a");
        Assert.assertTrue(result.getA().getClass() == A.class, "result.a.class == A");
        Assert.assertNotNull(result.getA().getD1(), "result.a.d1");
        Assert.assertEquals(result.getA().getD1().getNumber(), 0, "result.a.d1.number");
        Assert.assertEquals(result.getA().getD1().getString(), "name", "result.a.d1.string");
    }

    @Test
    public void createC_withB() throws Exception {
        Creator creator = new Creator();

        Instance<D> d1 = new Instance<D>(D.class, new Property(5));
        Instance<D> d2 = new Instance<D>(D.class, new Property("name"));
        Instance<B> b = new Instance<B>(B.class, new Compound(D.class, d1), new Compound(D.class, d2));
        Instance<C> c = new Instance<C>(C.class, new Compound(A.class, b));

        C result = creator.create(c);

        Assert.assertNotNull(result, "result");
        Assert.assertNotNull(result.getA(), "result.a");
        Assert.assertTrue(result.getA().getClass() == B.class, "result.a.class == B");
        Assert.assertNotNull(result.getA().getD1(), "result.a.d1");
        Assert.assertNotNull(((B) result.getA()).getD2(), "result.a.d2");
        Assert.assertEquals(result.getA().getD1().getNumber(), 5, "result.a.d1.number");
        Assert.assertEquals(result.getA().getD1().getString(), "", "result.a.d1.string");
        Assert.assertEquals(((B) result.getA()).getD2().getNumber(), 0, "result.a.d2.number");
        Assert.assertEquals(((B) result.getA()).getD2().getString(), "name", "result.a.d2.string");
    }

    @Test
    public void createB_SameInstanceForD_CreatedTheSame() throws Exception {
        Creator creator = new Creator();

        Instance<D> d = new Instance<D>(D.class, new Property(5));
        Instance<B> b = new Instance<B>(B.class, new Compound(D.class, d), new Compound(D.class, d));

        B result = creator.create(b);

        Assert.assertNotNull(result, "result");
        Assert.assertNotNull(result.getD1(), "result.d1");
        Assert.assertNotNull(result.getD2(), "result.d2");
        Assert.assertSame(result.getD1(), result.getD2(), "result.d1 == result.d2");
    }

    @Test
    public void createB_DifferentInstanceForD_AreTheSame() throws Exception {
        Creator creator = new Creator();

        Instance<D> d1 = new Instance<D>(D.class, new Property(5));
        Instance<D> d2 = new Instance<D>(D.class, new Property(5));
        Instance<B> b = new Instance<B>(B.class, new Compound(D.class, d1), new Compound(D.class, d2));

        B result = creator.create(b);

        Assert.assertNotNull(result, "result");
        Assert.assertNotNull(result.getD1(), "result.d1");
        Assert.assertNotNull(result.getD2(), "result.d2");
        Assert.assertNotSame(result.getD1(), result.getD2(), "result.d1 != result.d2");
    }

    @Test
    public void createA_nullAsValue() throws Exception {
        Creator creator = new Creator();

        Instance<A> a = new Instance<A>(A.class, new Null(D.class));

        A result = creator.create(a);

        Assert.assertNotNull(result, "result");
        Assert.assertNull(result.getD1(), "result.d1");
    }

    @Test(expectedExceptions = CreationException.class)
    public void createA_invalidCtor_exception() throws Exception {
        Creator creator = new Creator();

        Instance<A> a = new Instance<A>(A.class, new Property(3));

        creator.create(a);
    }

    @Test
    public void createD_invalidCtor_exceptionContainsD() {
        Creator c = new Creator();

        Instance<D> d = new Instance<D>(D.class, new Property(5), new Property(6));

        try {
            c.create(d);
        } catch (CreationException e) {
            List<Instance<?>> stack = e.getInstantiationStack();

            Assert.assertEquals(stack.size(), 1, "stack.size");
            Assert.assertEquals(stack.get(0), d, "stack[0] == d");
        }
    }

    @Test
    public void createA_invalidCtor_exceptionContainsDandA() {
        Creator c = new Creator();

        Instance<D> d = new Instance<D>(D.class, new Property(5), new Property(6));
        Instance<A> a = new Instance<A>(A.class, new Compound(D.class, d));

        try {
            c.create(a);
        } catch (CreationException e) {
            List<Instance<?>> stack = e.getInstantiationStack();

            Assert.assertEquals(stack.size(), 2, "stack.size");
            Assert.assertEquals(stack.get(0), a, "stack[0] == a");
            Assert.assertEquals(stack.get(1), d, "stack[1] == d");
        }
    }
}
