package flow;

import java.util.Collection;

/**
 * Created by Tzachi on 12/29/14.
 */
public abstract class EntryRouteUnit<T> extends RouteUnit<T> {
    public EntryRouteUnit() {
        super(null);
    }

    public abstract Collection<RouteUnit<T>> calculateRoutesFor(WorkUnit<T> work);
}
