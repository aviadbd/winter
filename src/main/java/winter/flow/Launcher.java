package winter.flow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by aviadbendov on 1/3/15.
 */
final class Launcher<T> {
    private final Queue<LogicUnitLauncher> freeLaunchers = new LinkedList<LogicUnitLauncher>();
    private final Map<Future<LogicUnitLaunchResult>, LogicUnitLauncher> usedLaunchers = new HashMap<Future<LogicUnitLaunchResult>, LogicUnitLauncher>();
    private final CompletionService<LogicUnitLaunchResult> executor;
    private final BlockingQueue<WorkUnit<T>> queue;
    private final ExceptionHandler exceptionHandler;

    public Launcher(Executor executor, BlockingQueue<WorkUnit<T>> queue, ExceptionHandler exceptionHandler) {
        this.queue = queue;
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
