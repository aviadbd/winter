package flow;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by Tzachi on 12/27/14.
 */

@PrepareForTest(RouteUnit.class)
public class RouteUnitTest extends PowerMockTestCase{

    @Test
    @SuppressWarnings("unchecked")
    public void getLogicUnit_null() throws Exception {
        RouteUnit<String> ru = Mockito.mock(RouteUnit.class);
        Assert.assertNull(ru.getLogicUnit());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getLogicUnit_mockedLogicUnit() throws Exception {
        RouteUnit<String> ru = PowerMockito.mock(RouteUnit.class);
        LogicUnit<String> lu = PowerMockito.mock(LogicUnit.class);
        PowerMockito.when(ru.getLogicUnit()).thenReturn(lu);
        Assert.assertSame(lu, ru.getLogicUnit());
    }



}
