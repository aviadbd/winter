package creation;

import model.Instance;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * A specialized exception for the {@link Creator} class. This exception class contains a
 * stack (see {@link #getInstantiationStack()}) for the {@link model.Instance} items that were being created when the exception
 * was raised.
 *
 * @author aviad
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
