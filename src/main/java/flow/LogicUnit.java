package flow;

import java.util.Queue;

/**
 * User-code should be written by extending this abstract class.
 */
public abstract class LogicUnit<T> {

    /**
     * Performs the user logic on the presented work, pushing into queue further works to be made.
     * @param work The work unit to perform logic on.
     * @param queue Push new work items, or the work presented, back to this queue for further processing. Note that
     *              once a {@link WorkUnit} is added to the queue, it should not be changed for thread-safety reasons.
     */
    public abstract void execute(WorkUnit<T> work, ProducerQueue<T> queue);
}
