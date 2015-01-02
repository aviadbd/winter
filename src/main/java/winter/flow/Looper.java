package winter.flow;

import javax.annotation.processing.Completion;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by aviadbendov on 12/27/14.
 */
public final class Looper<T> implements Runnable {
    private final RouteUnit<T> entry;
    private final BlockingQueue<WorkUnit<T>> queue;
    private final CompletionService<LogicUnitLaunchResult> executor;
    private final long QUEUE_WAITING_TIME = 100;
    private final ExceptionHandler exceptionHandler;
    private final Queue<LogicUnitLauncher> freeLaunchers = new LinkedList<LogicUnitLauncher>();
    private final Map<Future<LogicUnitLaunchResult>, LogicUnitLauncher> usedLaunchers = new HashMap<Future<LogicUnitLaunchResult>, LogicUnitLauncher>();
    private boolean shutdown = false;

    public Looper(RouteUnit<T> entry, BlockingQueue<WorkUnit<T>> queue, Executor executor, ExceptionHandler exceptionHandler) {
        this.entry = entry;
        this.executor = new ExecutorCompletionService<LogicUnitLaunchResult>(executor);
        this.queue = queue;
        this.exceptionHandler = exceptionHandler;
    }

    // Logic Unit Launcher pool

    @Override
    public void run() {
        try {
            while (!shutdown) {
                try {
                    dealWithFinishedLaunchers();

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
                        launch(data, nextRoute);
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

    private void launch(T data, RouteUnit<T> route) {
        LogicUnitLauncher launcher = freeLaunchers.poll();
        if (launcher == null) {
            launcher = new LogicUnitLauncher();
        }

        launcher.setRoute(route);
        launcher.setData(data);

        Future<LogicUnitLaunchResult> future = executor.submit(launcher);
        usedLaunchers.put(future, launcher);
    }

    private void dealWithFinishedLaunchers() {
        // deal with finished launchers, free other launchers
        Future<LogicUnitLaunchResult> finishedFuture;
        try {
            while ((finishedFuture = executor.poll()) != null) {
                assert finishedFuture.isDone();

                LogicUnitLauncher freeLauncher = usedLaunchers.remove(finishedFuture);
                freeLaunchers.add(freeLauncher);

                try {
                    LogicUnitLaunchResult result = finishedFuture.get();

                    for (T resultData : result.results) {
                        WorkUnit<T> newWork = new WorkUnit<T>(resultData);
                        newWork.setCurrentLocation(result.route);
                        queue.add(newWork);
                    }
                } catch (ExecutionException e) {
                    if (exceptionHandler != null) {
                        exceptionHandler.handleException(e);
                    }
                }
            }
        } catch (InterruptedException e) {
            // OK.
            System.out.println("launch loop interrupted");
        }
    }

    private class LogicUnitLaunchResult {
        public final T[] results;
        public final RouteUnit<T> route;

        public LogicUnitLaunchResult(T[] results, RouteUnit<T> route) {
            this.results = results;
            this.route = route;
        }
    }

    private class LogicUnitLauncher implements Callable<LogicUnitLaunchResult> {
        private T data;
        private RouteUnit<T> route;

        public void setRoute(RouteUnit<T> route) {
            this.route = route;
        }

        public void setData(T data) {
            this.data = data;
        }

        @Override
        public LogicUnitLaunchResult call() {
            return new LogicUnitLaunchResult(route.getLogicUnit().execute(data), route);
        }
    }


}
