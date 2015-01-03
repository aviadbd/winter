package winter.flow;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by aviadbendov on 12/27/14.
 */
public final class Looper<T> implements Runnable {
    private final RouteUnit<T> entry;
    private final BlockingQueue<WorkUnit<T>> queue = new LinkedBlockingQueue<WorkUnit<T>>();
    private final long QUEUE_WAITING_TIME = 100;
    private final ExceptionHandler exceptionHandler;
    private final Launcher<T> launcher;
    private boolean shutdown = false;

    public Looper(RouteUnit<T> entry, Executor executor, ExceptionHandler exceptionHandler) {
        this.entry = entry;
        this.launcher = new Launcher<T>(this, executor, exceptionHandler);
        this.exceptionHandler = exceptionHandler;
    }

    public void createWork(T data) {
        queue.add(new WorkUnit<T>(data));
    }

    /*package*/ void createWork(T data, RouteUnit<T> route) {
        WorkUnit<T> workUnit = new WorkUnit<T>(data);
        workUnit.setCurrentLocation(route);
        queue.add(workUnit);
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                try {
                    launcher.cleanup();

                    final WorkUnit<T> work = queue.poll(QUEUE_WAITING_TIME, TimeUnit.MILLISECONDS);
                    if (work == null) {
                        continue;
                    }
                    final T data = work.getData();

                    RouteUnit<T> currentRoute = work.getCurrentRoute();

                    Iterable<RouteUnit<T>> nextRoutes = currentRoute == null
                            ? Collections.singleton(entry)
                            : currentRoute.calculateRoutesFor(work);

                    for (final RouteUnit<T> nextRoute : nextRoutes) {
                        launcher.launch(data, nextRoute);
                    }
                } catch (RuntimeException e) {
                    if (exceptionHandler != null) {
                        exceptionHandler.handleException(e);
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
