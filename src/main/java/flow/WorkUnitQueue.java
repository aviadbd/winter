package flow;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by aviadbendov on 12/27/14.
 */
final class WorkUnitQueue<T> implements ProducerQueue<T>, BlockingQueue<WorkUnit<T>> {
    private final BlockingQueue<WorkUnit<T>> delegate;

    public WorkUnitQueue(BlockingQueue<WorkUnit<T>> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean add(WorkUnit<T> tWorkUnit) {
        return delegate.add(tWorkUnit);
    }

    @Override
    public boolean offer(WorkUnit<T> tWorkUnit) {
        return delegate.offer(tWorkUnit);
    }

    @Override
    public void put(WorkUnit<T> tWorkUnit) throws InterruptedException {
        delegate.put(tWorkUnit);
    }

    @Override
    public boolean offer(WorkUnit<T> tWorkUnit, long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.offer(tWorkUnit, timeout, unit);
    }

    @Override
    public WorkUnit<T> take() throws InterruptedException {
        return delegate.take();
    }

    @Override
    public WorkUnit<T> poll(long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.poll(timeout, unit);
    }

    @Override
    public int remainingCapacity() {
        return delegate.remainingCapacity();
    }

    @Override
    public WorkUnit<T> remove() {
        return delegate.remove();
    }

    @Override
    public WorkUnit<T> poll() {
        return delegate.poll();
    }

    @Override
    public WorkUnit<T> element() {
        return delegate.element();
    }

    @Override
    public WorkUnit<T> peek() {
        return delegate.peek();
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public int drainTo(Collection<? super WorkUnit<T>> c) {
        return delegate.drainTo(c);
    }

    @Override
    public int drainTo(Collection<? super WorkUnit<T>> c, int maxElements) {
        return delegate.drainTo(c, maxElements);
    }

    @Override
    public Iterator<WorkUnit<T>> iterator() {
        return delegate.iterator();
    }

    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return delegate.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        return delegate.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends WorkUnit<T>> c) {
        return delegate.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return delegate.retainAll(c);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
}
