package winter.config.creation;

import data.A;
import data.B;
import data.C;
import data.D;
import winter.config.model.Compound;
import winter.config.model.Instance;
import winter.config.model.Null;
import winter.config.model.Property;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.LinkedList;

import java.util.List;

/**
 * Created by aviadbendov on 11/20/14.
 */
public class CreatorTest {

    @Test
    public void createD_withInt() throws Exception {
        Creator c = new Creator();
        Instance<D> instance = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));

        D result = c.create(instance);

        Assert.assertNotNull(result, "result");
        Assert.assertEquals(result.getNumber(), 5, "result.number");
        Assert.assertEquals(result.getString(), "", "result.string");
    }

    @Test
    public void createD_withString() throws Exception {
        Creator c = new Creator();
        Instance<D> instance = new Instance<D>(D.class, new Property("name", Property.PrimitiveType.STRING));

        D result = c.create(instance);

        Assert.assertNotNull(result, "result");
        Assert.assertEquals(result.getNumber(), 0, "result.number");
        Assert.assertEquals(result.getString(), "name", "result.string");
    }

    @Test
    public void createA_withD_Int() throws Exception {
        Creator c = new Creator();

        Instance<D> d = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));
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

        Instance<D> d = new Instance<D>(D.class, new Property("name", Property.PrimitiveType.STRING));
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

        Instance<D> d = new Instance<D>(D.class, new Property("name", Property.PrimitiveType.STRING));
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

        Instance<D> d1 = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));
        Instance<D> d2 = new Instance<D>(D.class, new Property("name", Property.PrimitiveType.STRING));
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

        Instance<D> d = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));
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

        Instance<D> d1 = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));
        Instance<D> d2 = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));
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

        Instance<A> a = new Instance<A>(A.class, new Property(3, Property.PrimitiveType.INTEGER));

        creator.create(a);
    }

    @Test
    public void createD_invalidCtor_exceptionContainsD() {
        Creator c = new Creator();

        Instance<D> d = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER),
                new Property(6, Property.PrimitiveType.INTEGER));

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

        Instance<D> d = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER),
                new Property(6, Property.PrimitiveType.INTEGER));
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

    @Test
    public void createB_invalidCtorFirstD_exceptionContainsD1() {
        Creator c = new Creator();

        Instance<D> d1 = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER),
                new Property(6, Property.PrimitiveType.INTEGER));
        Instance<D> d2 = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));
        Instance<B> b = new Instance<B>(B.class, new Compound(D.class, d1), new Compound(D.class, d2));

        try {
            c.create(b);
        } catch (CreationException e) {
            List<Instance<?>> stack = e.getInstantiationStack();

            Assert.assertEquals(stack.size(), 2, "stack.size");
            Assert.assertEquals(stack.get(0), b, "stack[0] == b");
            Assert.assertEquals(stack.get(1), d1, "stack[1] == d1");
        }
    }

    @Test
    public void createB_invalidCtorFirstD_exceptionContainsD2() {
        Creator c = new Creator();

        Instance<D> d1 = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));
        Instance<D> d2 = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER),
                new Property(6, Property.PrimitiveType.INTEGER));
        Instance<B> b = new Instance<B>(B.class, new Compound(D.class, d1), new Compound(D.class, d2));

        try {
            c.create(b);
        } catch (CreationException e) {
            List<Instance<?>> stack = e.getInstantiationStack();

            Assert.assertEquals(stack.size(), 2, "stack.size");
            Assert.assertEquals(stack.get(0), b, "stack[0] == b");
            Assert.assertEquals(stack.get(1), d2, "stack[1] == d2");
        }
    }

    @Test
    public void addPreListener_true() throws Exception {
        Creator creator = new Creator();

        PreInstantiationListener pil = new MyPreInstantiationListener(true);

        Assert.assertTrue(creator.addPreInstantiationListener(pil));
    }

    @Test
    public void addPreListenerTwice_false() throws Exception {
        Creator creator = new Creator();

        PreInstantiationListener pil = new MyPreInstantiationListener(true);

        creator.addPreInstantiationListener(pil);
        Assert.assertFalse(creator.addPreInstantiationListener(pil));
    }

    @Test
    public void addPreListenerAndRemove_true() {
        Creator creator = new Creator();

        PreInstantiationListener pil = new MyPreInstantiationListener(true);

        creator.addPreInstantiationListener(pil);
        Assert.assertTrue(creator.removePreInstantiationListener(pil));
    }

    @Test
    public void removePreListenerWithoutAdding_false() {
        Creator creator = new Creator();

        PreInstantiationListener pil = new MyPreInstantiationListener(true);

        Assert.assertFalse(creator.removePreInstantiationListener(pil));
    }

    @Test
    public void addPreListener_removeTwice_false() {
        Creator creator = new Creator();

        PreInstantiationListener pil = new MyPreInstantiationListener(true);

        creator.addPreInstantiationListener(pil);
        creator.removePreInstantiationListener(pil);
        Assert.assertFalse(creator.removePreInstantiationListener(pil));
    }

    @Test
    public void addPreListener_TwoDifferent_BothTrue() {
        Creator creator = new Creator();

        PreInstantiationListener pil1 = new MyPreInstantiationListener(true);
        PreInstantiationListener pil2 = new MyPreInstantiationListener(true);

        Assert.assertTrue(creator.addPreInstantiationListener(pil1), "pil1");
        Assert.assertTrue(creator.addPreInstantiationListener(pil2), "pil2");
    }

    @Test
    public void addPostListener_true() throws Exception {
        Creator creator = new Creator();

        PostInstantiationListener pil = new MyPostInstantiationListener();

        Assert.assertTrue(creator.addPostInstantiationListener(pil));
    }

    @Test
    public void addPostListenerTwice_false() throws Exception {
        Creator creator = new Creator();

        PostInstantiationListener pil = new MyPostInstantiationListener();

        creator.addPostInstantiationListener(pil);
        Assert.assertFalse(creator.addPostInstantiationListener(pil));
    }

    @Test
    public void addPostListenerAndRemove_true() {
        Creator creator = new Creator();

        PostInstantiationListener pil = new MyPostInstantiationListener();

        creator.addPostInstantiationListener(pil);
        Assert.assertTrue(creator.removePostInstantiationListener(pil));
    }

    @Test
    public void removePostListenerWithoutAdding_false() {
        Creator creator = new Creator();

        PostInstantiationListener pil = new MyPostInstantiationListener();

        Assert.assertFalse(creator.removePostInstantiationListener(pil));
    }

    @Test
    public void addPostListener_removeTwice_false() {
        Creator creator = new Creator();

        PostInstantiationListener pil = new MyPostInstantiationListener();

        creator.addPostInstantiationListener(pil);
        creator.removePostInstantiationListener(pil);
        Assert.assertFalse(creator.removePostInstantiationListener(pil));
    }

    @Test
    public void addPostListener_TwoDifferent_BothTrue() {
        Creator creator = new Creator();

        PostInstantiationListener pil1 = new MyPostInstantiationListener();
        PostInstantiationListener pil2 = new MyPostInstantiationListener();

        Assert.assertTrue(creator.addPostInstantiationListener(pil1), "pil1");
        Assert.assertTrue(creator.addPostInstantiationListener(pil2), "pil2");
    }

    @Test
    public void addPreListener_createD_instanceCaptured() throws CreationException {
        Creator c = new Creator();
        Instance<D> instance = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));

        MyPreInstantiationListener pil = new MyPreInstantiationListener(true);
        c.addPreInstantiationListener(pil);
        c.create(instance);

        Assert.assertEquals(pil.getInstances().size(), 1, "instances.length");
        Assert.assertTrue(pil.getInstances().contains(instance), "instances.contains(D)");
    }

    @Test
    public void addPostListener_createD_instanceAndObjectCaptured() throws CreationException {
        Creator c = new Creator();
        Instance<D> instance = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));

        MyPostInstantiationListener pil = new MyPostInstantiationListener();
        c.addPostInstantiationListener(pil);

        D result = c.create(instance);

        Assert.assertEquals(pil.getInstances().size(), 1, "instances.length");
        Assert.assertEquals(pil.getObjects().size(), 1, "objects.length");
        Assert.assertTrue(pil.getInstances().contains(instance), "instances.contains(D)");
        Assert.assertTrue(pil.getObjects().contains(result), "objects.contains(D)");
    }

    @Test
    public void createB_differentD_listenersCapture() throws CreationException {
        Creator creator = new Creator();

        Instance<D> d1 = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));
        Instance<D> d2 = new Instance<D>(D.class, new Property("name", Property.PrimitiveType.STRING));
        Instance<B> b = new Instance<B>(B.class, new Compound(D.class, d1), new Compound(D.class, d2));

        MyPreInstantiationListener pre = new MyPreInstantiationListener(true);
        MyPostInstantiationListener post = new MyPostInstantiationListener();

        creator.addPreInstantiationListener(pre);
        creator.addPostInstantiationListener(post);

        B result = creator.create(b);

        Assert.assertEquals(pre.getInstances().size(), 3, "pre.instances.size");
        Assert.assertEquals(post.getInstances().size(), 3, "post.instances.size");
        Assert.assertEquals(post.getObjects().size(), 3, "post.objects.size");

        Assert.assertTrue(pre.getInstances().contains(d1), "pre.instances.contains(d1)");
        Assert.assertTrue(pre.getInstances().contains(d2), "pre.instances.contains(d2)");
        Assert.assertTrue(pre.getInstances().contains(b), "pre.instances.contains(b)");

        Assert.assertTrue(post.getInstances().contains(d1), "post.instances.contains(d1)");
        Assert.assertTrue(post.getInstances().contains(d2), "post.instances.contains(d2)");
        Assert.assertTrue(post.getInstances().contains(b), "post.instances.contains(b)");

        Assert.assertTrue(post.getObjects().contains(result.getD1()), "post.objects.contains(b.d1)");
        Assert.assertTrue(post.getObjects().contains(result.getD2()), "post.objects.contains(b.d2)");
        Assert.assertTrue(post.getObjects().contains(result), "post.objects.contains(b)");
    }

    @Test
    public void createB_sameD_listenersCapture() throws CreationException {
        Creator creator = new Creator();

        Instance<D> d = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));
        Instance<B> b = new Instance<B>(B.class, new Compound(D.class, d), new Compound(D.class, d));

        MyPreInstantiationListener pre = new MyPreInstantiationListener(true);
        MyPostInstantiationListener post = new MyPostInstantiationListener();

        creator.addPreInstantiationListener(pre);
        creator.addPostInstantiationListener(post);

        B result = creator.create(b);

        Assert.assertEquals(pre.getInstances().size(), 2, "pre.instances.size");
        Assert.assertEquals(post.getInstances().size(), 2, "post.instances.size");
        Assert.assertEquals(post.getObjects().size(), 2, "post.objects.size");

        Assert.assertTrue(pre.getInstances().contains(d), "pre.instances.contains(d1)");
        Assert.assertTrue(pre.getInstances().contains(b), "pre.instances.contains(b)");

        Assert.assertTrue(post.getInstances().contains(d), "post.instances.contains(d1)");
        Assert.assertTrue(post.getInstances().contains(b), "post.instances.contains(b)");

        Assert.assertTrue(post.getObjects().contains(result.getD1()), "post.objects.contains(b.d1)");
        Assert.assertTrue(post.getObjects().contains(result.getD2()), "post.objects.contains(b.d2)");
        Assert.assertTrue(post.getObjects().contains(result), "post.objects.contains(b)");
    }

    @Test(expectedExceptions = CreationException.class, expectedExceptionsMessageRegExp = "Bad")
    public void preListenerThrowsCreationException_propagates() throws CreationException {
        Creator c = new Creator();
        Instance<D> instance = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));

        MyPreInstantiationListener pil = new MyPreInstantiationListener(false);
        c.addPreInstantiationListener(pil);
        c.create(instance);
    }

    @Test(expectedExceptions = CreationException.class)
    public void preListenerThrowsRuntimeException_wrapped() throws CreationException {
        Creator c = new Creator();
        Instance<D> instance = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));

        PreInstantiationListener pil = new PreInstantiationListener() {
            @Override
            public void willCreateInstance(Instance<?> instance) throws CreationException {
                throw new RuntimeException();
            }
        };

        c.addPreInstantiationListener(pil);
        c.create(instance);
    }

    @Test
    public void proof_changeParameter() throws CreationException {
        Creator c = new Creator();
        Instance<D> instance = new Instance<D>(D.class, new Property(5, Property.PrimitiveType.INTEGER));

        PreInstantiationListener pil = new PreInstantiationListener() {
            @Override
            public void willCreateInstance(Instance<?> instance) throws CreationException {
                instance.getParameters()[0] = new Property(6, Property.PrimitiveType.INTEGER);
            }
        };

        c.addPreInstantiationListener(pil);

        D result = c.create(instance);

        Assert.assertEquals(result.getNumber(), 6);
    }

    private static class MyPreInstantiationListener implements PreInstantiationListener {
        private final boolean value;
        private final Collection<Instance<?>> instances = new LinkedList<Instance<?>>();

        public MyPreInstantiationListener(boolean value) {
            this.value = value;
        }

        @Override
        public void willCreateInstance(Instance<?> instance) throws CreationException {
            instances.add(instance);

            if (!value) {
                throw new CreationException("Bad");
            }
        }

        public Collection<Instance<?>> getInstances() {
            return instances;
        }
    }

    private static class MyPostInstantiationListener implements PostInstantiationListener {
        private final Collection<Instance<?>> instances = new LinkedList<Instance<?>>();
        private final Collection<Object> objects = new LinkedList<Object>();

        @Override
        public void didCreateInstance(Instance<?> instance, Object object) {
            instances.add(instance);
            objects.add(object);
        }

        public Collection<Instance<?>> getInstances() {
            return instances;
        }

        public Collection<Object> getObjects() {
            return objects;
        }
    }
}
