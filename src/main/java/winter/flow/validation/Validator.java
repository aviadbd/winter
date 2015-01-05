package winter.flow.validation;

import winter.flow.LogicUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tzachi on 03/01/2015.
 *
 */

/**
 * TODO: how to overcome different parameter names in different logic classes
 * TODO: how to overcome multiple parameters (from the same/different type)
 */

public class Validator<T> {
    ScriptEngineManager factory = new ScriptEngineManager();
    Map<String, T> objectMap = new HashMap<String, T>();

    public void validate(LogicUnit<T> input, Class<T> tClass) throws ScriptException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        ScriptEngine engine = factory.getEngineByName("groovy");
        if (null == engine) {
            System.err.println("Could not find groovy script engine,make sure to include groovy engine in your classpath");
            return;
        }
        // TODO: from some reason every function is doubled in reflection (one with object and one with the actual type)
        // TODO: support more than one method
        Method method = input.getClass().getDeclaredMethod("execute", tClass);
        Parameter parameter = method.getParameters()[0];
        String name = parameter.getName();
        PreCondition preCondition = method.getAnnotation(PreCondition.class);
        if (preCondition != null) {
            //  retrieve parameters names from function and put them in engine if needed
            T t = objectMap.get(name);
            if (t == null) {
                t = (T) parameter.getType().newInstance();
                objectMap.put(name, t);
                engine.put(name, t);
            }
            String expression = preCondition.expression();
            System.out.println("***Pre***");
            System.out.println(engine.eval(expression));
            assert (Boolean) engine.eval(expression);
        }
        PostCondition postCondition = method.getAnnotation(PostCondition.class);
        if (preCondition != null) {
            // TODO: retrieve parameters names from function and put them in engine if needed
            String expression = postCondition.expression();
            System.out.println("***Post***");
            System.out.println(expression);
            String assignment = SimpleExpressionSplitters.CONTAINS.translate(expression);
            System.out.println(assignment);
            engine.eval(assignment);
            System.out.println(objectMap.get(name));
            System.out.println("***End***");
        }
    }
}
