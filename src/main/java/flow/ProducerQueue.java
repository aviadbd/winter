package flow;

/**
 * Created by aviadbendov on 12/27/14.
 */
public interface ProducerQueue<T> {
    boolean add(WorkUnit<T> work);
}
