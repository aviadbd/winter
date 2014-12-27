package flow;

import java.util.Queue;

/**
 * Created by aviadbendov on 12/27/14.
 */
public abstract class LogicUnit<T> {
    public abstract void execute(WorkUnit<T> work, ProducerQueue queue);
}
