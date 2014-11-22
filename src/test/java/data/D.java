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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        D d = (D) o;

        if (number != d.number) return false;
        if (string != null ? !string.equals(d.string) : d.string != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + (string != null ? string.hashCode() : 0);
        return result;
    }
}
