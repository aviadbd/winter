package winter.flow.validation;

import winter.flow.LogicUnit;

/**
 * Created by Tzachi on 05/01/2015.
 */
public class UnsupportedPostcondition extends LogicUnit<A> {
    @Override
    @PostCondition(expression = "work.map.size() > 0")
    public A[] execute(A work) {
        return new A[0];
    }
}
