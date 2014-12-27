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
                WorkUnit<T> work = queue.take();
                RouteUnit<T> currentLocation = work.getCurrentLocation();
                if (currentLocation == null) {
                    currentLocation = entry;
                }

                for (final WorkUnit<T> updatedWork : currentLocation.updateRouteFor(work)) {
                    executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            updatedWork.getCurrentLocation().getLogicUnit().execute(updatedWork, queue);
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
