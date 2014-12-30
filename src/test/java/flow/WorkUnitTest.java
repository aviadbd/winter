package flow;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Tzachi on 12/27/14.
 */
public class WorkUnitTest {

    @Test
    public void getData_null(){
        WorkUnit<String> wu = new WorkUnit<>(null);
        Assert.assertNull(wu.getData());
    }

    @Test
    public void getData_string(){
        String hello = "HELLO";
        WorkUnit<String> wu = new WorkUnit<>(hello);
        Assert.assertEquals(wu.getData(), hello);
    }

    @Test
    public void getCurrentLocation_null(){
        WorkUnit<String> wu = new WorkUnit<>("test");
        Assert.assertNull(wu.getCurrentLocation());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void setCurrentLocation_getCurrentLocation_notNull(){
        WorkUnit<String> wu = new WorkUnit<>("test");
        RouteUnit<String> ru = Mockito.mock(RouteUnit.class);
        wu.setCurrentLocation(ru);
        Assert.assertSame(wu.getCurrentLocation(), ru);
    }
}
