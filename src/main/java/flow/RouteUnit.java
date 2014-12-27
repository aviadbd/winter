package flow;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Used to create a graph of routes between different logic units. Routes are connected to each other,
 * and contain a logic unit to run. The {@link flow.WorkUnit} points to the route it currently resides in,
 * and the {@link flow.Looper} uses that route to move that work forward in the routes' graph.
 */
public abstract class RouteUnit<T> {
    private final LogicUnit<T> logicUnit;

    public RouteUnit(LogicUnit<T> logicUnit) {
        this.logicUnit = logicUnit;
    }

    /**
     * Returns {@link flow.WorkUnit}s whose {@link flow.WorkUnit#getCurrentLocation} returns the next route
     * for them to follow.
     * @param work The work to update.
     * @return New works, updated with their new locations.
     */
    public Iterable<WorkUnit<T>> updateRouteFor(WorkUnit<T> work) {
        Collection<WorkUnit<T>> newWorks = new LinkedList<WorkUnit<T>>();

        for (RouteUnit<T> routeUnit : calculateRoutesFor(work)) {
            WorkUnit<T> newWork = work.createCopy();
            newWork.setCurrentLocation(routeUnit);

            newWorks.add(newWork);
        }

        return newWorks;
    }

    /**
     * Implementors of {@link flow.RouteUnit} should implement this method to perform route calculations on the {@link flow.WorkUnit}
     * @param work The work to calculate a route for.
     * @return A collection of routes for the work unit to go to.
     */
    protected abstract Collection<RouteUnit<T>> calculateRoutesFor(WorkUnit<T> work);

    /**
     * Returns the {@link flow.LogicUnit} associated with this location.
     * @return The associated logic unit.
     */
    public final LogicUnit<T> getLogicUnit() {
        return logicUnit;
    }
}
