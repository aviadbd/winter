package winter.flow;

/**
 * State for the winter.flow's state machine. Contains data (see {@link #getData}), and the current location the state is in.
 * User-code can access the data, but cannot change the location.
 *
 * @see winter.flow.RouteUnit
 * @see winter.flow.LogicUnit
 */
class WorkUnit<T> {
    private final T data;
    private RouteUnit<T> currentLocation;

    WorkUnit(T data) {
        this.data = data;
    }

    /**
     * Retrieves the data contained within the work unit.
     * @return The data.
     */
    T getData() {
        return data;
    }

    // PACKAGE LEVEL ON PURPOSE

    RouteUnit<T> getCurrentRoute() {
        return currentLocation;
    }

    void setCurrentLocation(RouteUnit<T> currentLocation) {
        this.currentLocation = currentLocation;
    }

}
