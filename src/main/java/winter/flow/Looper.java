package winter.flow;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by aviadbendov on 12/27/14.
 *
 */
public final class Looper<T> implements Runnable {
    private final RouteUnit<T> entry;
    private final BlockingQueue<WorkUnit<T>> queue;
    private final ExecutorService executor;
    private final long QUEUE_WAITING_TIME = 100;
    private final RuntimeExceptionHandler runtimeExceptionHandler;
    private boolean shutdown = false;

    public Looper(RouteUnit<T> entry, BlockingQueue<WorkUnit<T>> queue, ExecutorService executor, RuntimeExceptionHandler runtimeExceptionHandler) {
        this.entry = entry;
        this.executor = executor;
        this.queue = queue;
        this.runtimeExceptionHandler = runtimeExceptionHandler;
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                try {
                    final WorkUnit<T> work = queue.poll(QUEUE_WAITING_TIME, TimeUnit.MILLISECONDS);
                    if (work == null){
                        continue;
                    }
                    final T data = work.getData();

                    RouteUnit<T> currentRoute = work.getCurrentRoute();

                    Iterable<RouteUnit<T>> nextRoutes = currentRoute == null
                            ? Collections.singleton(entry)
                            : currentRoute.calculateRoutesFor(work);

                    for (final RouteUnit<T> nextRoute : nextRoutes) {
                        executor.submit(new Runnable() {
                            @Override
                            public void run() {
                                T[] results = nextRoute.getLogicUnit().execute(data);

                                for (T result : results) {
                                    WorkUnit<T> newWork = new WorkUnit<T>(result);
                                    newWork.setCurrentLocation(nextRoute);
                                    queue.add(newWork);
                                }
                            }
                        });
                    }
                } catch (RuntimeException e){
                    if (runtimeExceptionHandler != null) {
                        runtimeExceptionHandler.handleException(e);
                    }
                }
            }
        } catch (InterruptedException e) {
            // This is OK. Nothing should happen here.
        }
    }

    public void shutdown() {
        shutdown = true;
    }

}
