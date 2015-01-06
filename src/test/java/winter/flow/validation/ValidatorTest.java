package winter.flow.validation;

import org.testng.Assert;
import org.testng.annotations.Test;
import winter.flow.LogicUnit;

import javax.script.ScriptException;

/**
 * Created by Tzachi on 04/01/2015.
 */
public class ValidatorTest {

    public static final String SOME_VALUE = "some value";

    @Test
    public void singleLogic_failPrecondition_returnNull() throws NoSuchMethodException, InstantiationException, IllegalAccessException, ScriptException {
        Validator<A> validator = new Validator<A>(new A());
        LogicUnit<A> lu = new FailPrecondition();
        A a = null;
        try {
            a = validator.validate(lu);
        } catch (Exception ignored) {
        }
        Assert.assertNull(a);
    }

    @Test
    public void singleLogic_failPrecondition_exception() throws NoSuchMethodException, InstantiationException, IllegalAccessException, ScriptException {
        Validator<A> validator = new Validator<A>(new A());
        LogicUnit<A> lu = new FailPrecondition();
        Boolean exceptionThrown = false;
        String exceptionMessage = null;
        try {
            validator.validate(lu);
        } catch (Exception e) {
            exceptionThrown = true;
            exceptionMessage = e.getMessage();
        }
        Assert.assertTrue(exceptionThrown);
        Assert.assertEquals(Validator.PRECONDITION_FAILED, exceptionMessage);
    }

    @Test
    public void singleLogic_unsupportedPostcondition_returnNull() throws NoSuchMethodException, InstantiationException, IllegalAccessException, ScriptException {
        Validator<A> validator = new Validator<A>(new A());
        LogicUnit<A> lu = new UnsupportedPostcondition();
        A a = null;
        try {
            a = validator.validate(lu);
        } catch (Exception ignored) {
        }
        Assert.assertNull(a);
    }

    @Test
    public void singleLogic_unsupportedPostcondition_exception() throws NoSuchMethodException, InstantiationException, IllegalAccessException, ScriptException {
        Validator<A> validator = new Validator<A>(new A());
        LogicUnit<A> lu = new UnsupportedPostcondition();
        Boolean exceptionThrown = false;
        String exceptionMessage = null;
        try {
            validator.validate(lu);
        } catch (Exception e) {
            exceptionThrown = true;
            exceptionMessage = e.getMessage();
        }
        Assert.assertTrue(exceptionThrown);
        Assert.assertEquals(Validator.UNSUPPORTED_POSTCONDITION, exceptionMessage);
    }

    @Test
    public void singleLogic_checkPostConditionAssignments() throws IllegalAccessException, ScriptException, InstantiationException, NoSuchMethodException {
        Validator<A> validator = new Validator<A>(new A());
        LogicUnit<A> lu = new FirstLogic();
        A a = validator.validate(lu);
        Assert.assertEquals(SOME_VALUE, a.getMap().get("hello"));
    }

    @Test
    public void multipleLogic_failSecondPre() throws NoSuchMethodException, InstantiationException, IllegalAccessException, ScriptException {
        Validator<A> validator = new Validator<A>(new A());
        validator.validate(new FirstLogic());
        Boolean exceptionThrown = false;
        String exceptionMessage = null;
        try {
            validator.validate(new FailPrecondition());
        } catch (Exception e) {
            exceptionThrown = true;
            exceptionMessage = e.getMessage();
        }
        Assert.assertTrue(exceptionThrown);
        Assert.assertEquals(Validator.PRECONDITION_FAILED, exceptionMessage);
    }

    @Test
    public void multipleLogic_failSecondPost() throws NoSuchMethodException, InstantiationException, IllegalAccessException, ScriptException {
        Validator<A> validator = new Validator<A>(new A());
        validator.validate(new FirstLogic());
        Boolean exceptionThrown = false;
        String exceptionMessage = null;
        try {
            validator.validate(new UnsupportedPostcondition());
        } catch (Exception e) {
            exceptionThrown = true;
            exceptionMessage = e.getMessage();
        }
        Assert.assertTrue(exceptionThrown);
        Assert.assertEquals(Validator.UNSUPPORTED_POSTCONDITION, exceptionMessage);
    }

    @Test
    public void multipleLogic_checkPostConditionAssignments() throws NoSuchMethodException, InstantiationException, IllegalAccessException, ScriptException {
        Validator<A> validator = new Validator<A>(new A());
        validator.validate(new FirstLogic());
        A a = validator.validate(new SecondLogic());
        Assert.assertEquals(SOME_VALUE, a.getMap().get("hello"));
        Assert.assertEquals(SOME_VALUE, a.getMap().get("konnichiwa"));
    }

}