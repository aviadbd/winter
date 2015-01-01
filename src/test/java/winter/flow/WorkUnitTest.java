package winter.flow;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Tzachi on 12/27/14.
 */
public class WorkUnitTest {

    @Test
    public void getData_null(){
        WorkUnit<String> wu = new WorkUnit<String>(null);
        Assert.assertNull(wu.getData());
    }

    @Test
    public void getData_string(){
        String hello = "HELLO";
        WorkUnit<String> wu = new WorkUnit<String>(hello);
        Assert.assertEquals(wu.getData(), hello);
    }

    @Test
    public void getCurrentLocation_null(){
        WorkUnit<String> wu = new WorkUnit<String>("test");
        Assert.assertNull(wu.getCurrentRoute());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void setCurrentLocation_getCurrentLocation_notNull(){
        WorkUnit<String> wu = new WorkUnit<String>("test");
        RouteUnit<String> ru = Mockito.mock(RouteUnit.class);
        wu.setCurrentLocation(ru);
        Assert.assertSame(wu.getCurrentRoute(), ru);
    }
}
