package data;

/**
 * Created by aviadbendov on 1/3/15.
 */
public class E {
    private final F f;

    public E(F f) {
        this.f = f;
    }

    public F getEnum() {
        return f;
    }

    public static enum F {
        Value;
    }
}
