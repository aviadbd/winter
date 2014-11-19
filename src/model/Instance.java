package model;

import java.util.List;

/**
 * Created by aviadbendov on 11/19/14.
 */
public final class Instance {
    private final List<Parameter> parameters;

    public Instance(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }
}
