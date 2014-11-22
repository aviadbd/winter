package creation;

/**
 * Created by aviadbendov on 11/20/14.
 */
public class CreationException extends Exception {
    public CreationException(Throwable cause) {
        super(cause);
    }

    public CreationException(String message) {
        super(message);
    }
}
