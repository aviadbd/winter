package data;

/**
 * Created by aviadbendov on 11/19/14.
 */
public class B extends A {

    private final D d2;

    public B(D d1, D d2) {
        super(d1);

        this.d2 = d2;
    }

    public D getD2() {
        return d2;
    }
}
