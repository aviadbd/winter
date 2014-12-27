package flow;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Created by aviadbendov on 12/27/14.
 */
public final class Looper<T> implements Runnable {
    private final RouteUnit<T> entry;
    private final WorkUnitQueue<T> queue;
    private final ExecutorService executor;

    private boolean shutdown = false;

    public Looper(RouteUnit<T> entry, BlockingQueue<WorkUnit<T>> queue, ExecutorService executor) {
        this.entry = entry;
        this.executor = executor;
        this.queue = new WorkUnitQueue<T>(queue);
    }


    @Override
    public void run() {
        while (!shutdown) {
            try {
                final WorkUnit<T> work = queue.take();
                final T data = work.getData();

                RouteUnit<T> currentLocation = work.getCurrentLocation();
                if (currentLocation == null) {
                    currentLocation = entry;
                }

                for (final RouteUnit<T> nextLocation : currentLocation.calculateRoutesFor(work)) {
                    executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            T[] results = nextLocation.getLogicUnit().execute(data);

                            for (T result : results) {
                                WorkUnit<T> newWork = new WorkUnit<T>(result);
                                newWork.setCurrentLocation(nextLocation);
                                queue.add(newWork);
                            }
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setShutdown() {
        shutdown = true;
    }
}
