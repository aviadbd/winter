package winter.flow.validation;

import winter.flow.LogicUnit;

/**
 * Created by Tzachi on 05/01/2015.
 */
public class SecondLogic extends LogicUnit<A> {
    @Override
    @PreCondition(expression = "work.map.size() == 1")
    @PostCondition(expression = "work.map.konnichiwa != null")
    public A[] execute(A work) {
        work.getMap().put("konnichiwa", "aviad");
        return new A[]{work};
    }
}
