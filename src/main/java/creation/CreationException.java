package creation;

import model.Instance;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by aviadbendov on 11/20/14.
 */
public class CreationException extends Exception {
    private final List<Instance<?>> instantiationStack;

    public CreationException(String message) {
        this(message, Collections.<Instance<?>>emptyList());
    }

    public CreationException(Throwable cause, List<Instance<?>> instantiationStack) {
        super(cause);
        this.instantiationStack = new LinkedList<Instance<?>>(instantiationStack);
    }

    public CreationException(String message, List<Instance<?>> instantiationStack) {
        super(message);
        this.instantiationStack = new LinkedList<Instance<?>>(instantiationStack);
    }

    public List<Instance<?>> getInstantiationStack() {
        return instantiationStack;
    }
}
