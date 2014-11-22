/**
 * <h2>The model of Winter</h2>
 *
 * An {@link model.Instance} represents a constructor to be called, with all its arguments, represented by a list
 * of {@link model.Parameter} implementations.
 * An argument can be either a {@link model.Property} primitive (which, in Winter, could also be a {@link java.lang.String}), or another class.
 * Since a constructor could require one class/interface and due to class hierarchy the constructor is passed a child-class,
 * this is supported by using a bridge class, {@link model.Compound}. Compound encapsulates the argument required for
 * an Instance's constructor, while also linking to another Instance as the concrete implementation to be passed when
 * instantiating it. This also means Winter could instantiate two separate instances passing both the same instance
 * as a constructor argument.
 *
 * In short, {@link model.Instance} has a Many-to-Many relationship to itself, using a @{link model.Compound} as a bridge
 * (making it a One-to-Many relationship to Compounds and Many-to-One relationship between Compounds and an Instance).
 * It also has a One-to-Many relationship to {@link model.Property} objects, which are the primitives of Winter.
 *
 * <h2>Example</h2>
 *
 * Suppose we have four classes (each is described below with their constructors):
 * <pre><code>
 * class D { ctor(int), ctor(String) }
 *
 * class A { ctor(D) }
 *
 * class B extends A { ctor(D, D) }
 *
 * class C { ctor(A) }
 *
 * </code></pre>
 *
 * <p>
 * <b>Case 1:</b>
 * <pre><code>
 * D d = new D(5);
 * </code></pre>
 * Would be:
 * <pre><code>
 * Instance d = new Instance(D.class, new Property(5));
 * </code></pre>
 * </p>
 *
 * <p>
 * <b>Case 2:</b>
 * <pre><code>
 * D d = new D(5);
 * A a = new A(d);
 * </code></pre>
 *
 * Would be:
 *
 * <pre><code>
 * Instance d = new Instance(D.class, new Property(5));
 * Instance a = new Instance(A.class, new Compound(D.class, d));
 * </code></pre>
 *
 * </p>
 *
 * <p>
 * <b>Case 3:</b>
 * <pre><code>
 * D d1 = new D(5);
 * D d2 = new D("hello");
 * A a = new B(d1, d2);
 * C c = new C(a);
 * </code></pre>
 *
 * Would be:
 *
 * <pre><code>
 * Instance d1 = new Instance(D.class, new Property(5));
 * Instance d2 = new Instance(D.class, new Property("name"));
 * Instance a = new Instance(B.class, new Compound(D.class, d1), new Compound(D.class, d2));
 * Instance c = new Instance(C.class, new Compound(A.class, a));
 * </code></pre>
 *
 * Note how, even though <code>a</code> is in fact of type B, it can be passed to the <code>c</code> instance using the {@link model.Compound} bridge.
 * Also note how using the correct {@link model.Parameter} list, Winter chooses the correct constructor to use for instantiating <code>d1</code> and <code>d2</code>.
 *
 * </p>
 *
 */
package model;