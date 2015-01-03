package winter.flow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * The Launcher is the completing part to the {@link winter.flow.Looper} logic, as it sends {@link winter.flow.LogicUnit}
 * to the {@link java.util.concurrent.Executor} provided to the <code>Looper</code> class and then retrieves their
 * results, creating new {@link winter.flow.WorkUnit}s from them and adding them to the <code>Looper</code>'s queue.
 * <p/>
 * Note that the <code>Launcher</code> and <code>Looper</code> are very tightly coupled by design.
 */
final class Launcher<T> {
    private final Queue<LogicUnitLauncher> freeLaunchers = new LinkedList<LogicUnitLauncher>();
    private final Map<Future<LogicUnitLaunchResult>, LogicUnitLauncher> usedLaunchers = new HashMap<Future<LogicUnitLaunchResult>, LogicUnitLauncher>();
    private final CompletionService<LogicUnitLaunchResult> executor;
    private final ExceptionHandler exceptionHandler;
    private final Looper<T> looper;

    public Launcher(Looper<T> looper, Executor executor, ExceptionHandler exceptionHandler) {
        this.looper = looper;
        this.exceptionHandler = exceptionHandler;
        this.executor = new ExecutorCompletionService<LogicUnitLaunchResult>(executor);
    }

    public void launch(T data, RouteUnit<T> route) {
        LogicUnitLauncher launcher = freeLaunchers.poll();
        if (launcher == null) {
            launcher = new LogicUnitLauncher();
        }

        launcher.setRoute(route);
        launcher.setData(data);

        Future<LogicUnitLaunchResult> future = executor.submit(launcher);
        usedLaunchers.put(future, launcher);
    }

    public void cleanup() {
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
                        looper.createWork(resultData, result.route);
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
