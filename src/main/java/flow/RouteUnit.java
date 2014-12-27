package flow;

/**
 * Created by aviadbendov on 12/27/14.
 */
public abstract class RouteUnit<T> {
    private final LogicUnit<T> logicUnit;

    public RouteUnit(LogicUnit<T> logicUnit) {
        this.logicUnit = logicUnit;
    }

    public abstract Iterable<WorkUnit<T>> updateRouteFor(WorkUnit<T> work);

    public LogicUnit<T> getLogicUnit() {
        return logicUnit;
    }
}
