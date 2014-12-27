package flow;

/**
 * Created by aviadbendov on 12/27/14.
 */
public class WorkUnit<T> {
    private RouteUnit<T> currentLocation;


    // PACKAGE LEVEL ON PURPOSE

    RouteUnit<T> getCurrentLocation() {
        return currentLocation;
    }

    void setCurrentLocation(RouteUnit<T> currentLocation) {
        this.currentLocation = currentLocation;
    }
}
