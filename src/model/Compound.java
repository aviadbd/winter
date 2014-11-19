package model;

/**
 * Created by aviadbendov on 11/19/14.
 */
public final class Compound implements Parameter {

    private final Instance linkedInstance;

    public Compound(Instance linkedInstance) {
        this.linkedInstance = linkedInstance;
    }

    public Instance getLinkedInstance() {
        return linkedInstance;
    }
}
