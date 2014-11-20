package creation;

import data.D;
import model.Instance;
import model.Property;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by aviadbendov on 11/20/14.
 */
public class CreatorTest {

    @Test
    public void createD_withInt() throws Exception{
        Creator<D> c = new Creator<D>();
        Instance<D> instance = new Instance<D>(D.class, new Property(5));

        D result = c.create(instance);

        Assert.assertNotNull(result, "result");
        Assert.assertEquals(result.getNumber(), 5, "result.number");
        Assert.assertEquals(result.getString(), "", "result.string");
    }

    @Test
    public void createD_withString() throws Exception {
        Creator<D> c = new Creator<D>();
        Instance<D> instance = new Instance<D>(D.class, new Property("name"));

        D result = c.create(instance);

        Assert.assertNotNull(result, "result");
        Assert.assertEquals(result.getNumber(), 0, "result.number");
        Assert.assertEquals(result.getString(), "name", "result.string");
    }
}
