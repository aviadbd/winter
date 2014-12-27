package flow;

/**
 * State for the flow's state machine. Contains data (see {@link #getData}), and the current location the state is in.
 * User-code can access the data, but cannot change the location.
 *
 * @see flow.RouteUnit
 * @see flow.LogicUnit
 */
public class WorkUnit<T> {
    private final T data;
    private RouteUnit<T> currentLocation;

    public WorkUnit(T data) {
        this.data = data;
    }

    /**
     * Retrieves the data contained within the work unit.
     * @return The data.
     */
    public T getData() {
        return data;
    }

    /**
     * Creates a copy of the work unit. Data and location's pointers are copied.
     * @return The new work unit.
     */
    public WorkUnit<T> createCopy() {
        WorkUnit<T> copy = new WorkUnit<T>(this.data);
        copy.setCurrentLocation(this.getCurrentLocation());

        return copy;
    }

    // PACKAGE LEVEL ON PURPOSE

    RouteUnit<T> getCurrentLocation() {
        return currentLocation;
    }

    void setCurrentLocation(RouteUnit<T> currentLocation) {
        this.currentLocation = currentLocation;
    }

}
