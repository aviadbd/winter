package winter.flow.validation;

import winter.flow.LogicUnit;

/**
 * Created by Tzachi on 03/01/2015.
 *
 */
public class FirstLogic extends LogicUnit<A>{

    @Override
    @PreCondition(expression = "work.map.size() == 0")
    @PostCondition(expression = "work.map.hello != null")
    public A[] execute(A work) {
        work.getMap().put("hello", "tzachi");
        return new A[]{work};
    }
}