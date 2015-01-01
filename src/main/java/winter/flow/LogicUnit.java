package winter.flow;

/**
 * User-code should be written by extending this abstract class.
 */
public abstract class LogicUnit<T> {

    /**
     * Performs the user logic on the presented work, returning the data post-processing.
     * @param work The work unit to perform logic on.
     */
    public abstract T[] execute(T work);
}
