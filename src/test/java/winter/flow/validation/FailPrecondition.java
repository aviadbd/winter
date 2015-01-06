package winter.flow.validation;

import winter.flow.LogicUnit;

/**
 * Created by Tzachi on 05/01/2015.
 */
public class FailPrecondition extends LogicUnit<A> {
    @Override
    @PreCondition(expression = "work.map.size() == -4")
    public A[] execute(A work) {
        return new A[0];
    }
}
