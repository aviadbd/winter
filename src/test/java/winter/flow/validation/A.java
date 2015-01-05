package winter.flow.validation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tzachi on 03/01/2015.
 *
 */
public class A {
    private Map<String, String> map = new HashMap<String,String>();

    public Map<String, String> getMap() {
        return map;
    }

    @Override
    public String toString() {
        return "A{" +
                "map=" + map +
                '}';
    }
}
