package winter.flow;

import java.util.Collection;

/**
 * Used to create a graph of routes between different logic units. Routes are connected to each other,
 * and contain a logic unit to run. The {@link winter.flow.WorkUnit} points to the route it currently resides in,
 * and the {@link winter.flow.Looper} uses that route to move that work forward in the routes' graph.
 */
public abstract class RouteUnit<T> {
    private final LogicUnit<T> logicUnit;

    public RouteUnit(LogicUnit<T> logicUnit) {
        this.logicUnit = logicUnit;
    }

    /**
     * Implementers of {@link winter.flow.RouteUnit} should implement this method to perform route calculations on the {@link winter.flow.WorkUnit}
     * @param work The work to calculate a route for.
     * @return A collection of routes for the work unit to go to.
     */
    public abstract Collection<RouteUnit<T>> calculateRoutesFor(WorkUnit<T> work);

    /**
     * Returns the {@link winter.flow.LogicUnit} associated with this location.
     * @return The associated logic unit.
     */
    public final LogicUnit<T> getLogicUnit() {
        return logicUnit;
    }
}
