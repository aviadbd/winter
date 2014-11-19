package data;

/**
 * Created by aviadbendov on 11/19/14.
 */
public class D {
    private final int number;
    private final String string;

    public D(int number) {
        this.number = number;
        this.string = "";
    }

    public D(String string) {
        this.number = 0;
        this.string = string;
    }

    public int getNumber() {
        return number;
    }

    public String getString() {
        return string;
    }
}
