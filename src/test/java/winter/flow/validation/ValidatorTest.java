package winter.flow.validation;

import org.testng.annotations.Test;
import winter.flow.LogicUnit;

import javax.script.ScriptException;

/**
 * Created by Tzachi on 04/01/2015.
 *
 */
public class ValidatorTest {

    @Test
    public void bla() throws IllegalAccessException, ScriptException, InstantiationException, NoSuchMethodException {
        Validator<A> validator = new Validator<A>();
        LogicUnit<A> lu = new AddHello();
        validator.validate(lu, A.class);
    }
}
