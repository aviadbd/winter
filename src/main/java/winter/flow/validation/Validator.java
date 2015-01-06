package winter.flow.validation;

import winter.flow.LogicUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by Tzachi on 03/01/2015.
 *
 */

/**
 * TODO: how to overcome multiple parameters (from the same/different type)
 */

public class Validator<T> {

    public static final String PRECONDITION_FAILED = "Precondition Failed";
    public static final String UNSUPPORTED_POSTCONDITION = "UnsupportedPostcondition";
    private static final String NAME = "work";
    ScriptEngineManager factory = new ScriptEngineManager();
    private final T validatedObject;

    public Validator(T initialObject) {
        this.validatedObject = initialObject;
    }

    public T validate(LogicUnit<T> input) throws ScriptException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        ScriptEngine engine = factory.getEngineByName("groovy");
        if (null == engine) {
            System.err.println("Could not find groovy script engine,make sure to include groovy engine in your classpath");
            return null;
        }
        // TODO: from some reason every function is doubled in reflection (one with object and one with the actual type)
        // TODO: support more than one method
        Method method = input.getClass().getDeclaredMethod("execute", validatedObject.getClass());
        PreCondition preCondition = method.getAnnotation(PreCondition.class);
        if (preCondition != null) {
            //  retrieve parameters names from function and put them in engine if needed
            // TODO: how to overcome different parameter names in different logic classes
            engine.put(NAME, validatedObject);
            String expression = preCondition.expression();
            if (!(Boolean) engine.eval(expression)) {
                throw new RuntimeException(PRECONDITION_FAILED);
            }
        }
        PostCondition postCondition = method.getAnnotation(PostCondition.class);
        if (postCondition != null) {
            // TODO: retrieve parameters names from function and put them in engine if needed
            String expression = postCondition.expression();
            SimpleExpressionSplitter expressionSplitter = SimpleExpressionSplitter.findPattern(expression);
            if (expressionSplitter == null) {
                throw new RuntimeException(UNSUPPORTED_POSTCONDITION);
            }
            String assignment = expressionSplitter.translate(expression);
            engine.eval(assignment);
        }
        return validatedObject;
    }
}
