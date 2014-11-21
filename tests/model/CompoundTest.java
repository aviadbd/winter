package model;

import data.A;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by aviadbendov on 11/21/14.
 */
public class CompoundTest {

    @Test
    public void checkToString() {
        Compound compound = new Compound(A.class, null);

        Assert.assertEquals(compound.toString(), "[class data.A]");
    }
}
