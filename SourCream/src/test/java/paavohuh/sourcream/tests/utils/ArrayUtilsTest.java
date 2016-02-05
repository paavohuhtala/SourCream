
package paavohuh.sourcream.tests.utils;

import java.util.Arrays;
import java.util.Random;
import org.joou.UShort;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import paavohuh.sourcream.utils.ArrayUtils;

public class ArrayUtilsTest {
    
    public ArrayUtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void cloningByteArraysWorks() {
        Random rand = new Random(1122);
        byte[] buffer = new byte[128];
        rand.nextBytes(buffer);
        
        byte[] clone = ArrayUtils.clone(buffer);
        Assert.assertArrayEquals(buffer, clone);
        
        rand.nextBytes(buffer);
        Assert.assertFalse("Arrays shouldn't be equal after the original has been modified.", Arrays.equals(buffer, clone));
    }
    
    @Test
    public void cloningUShortArrayWorks() {
        Random rand = new Random(1122);
        UShort[] buffer = new UShort[128];
        
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = UShort.valueOf(rand.nextInt(UShort.MAX_VALUE));
        }
        
        UShort[] clone = ArrayUtils.clone(buffer);
        Assert.assertArrayEquals(buffer, clone);
        
        Arrays.setAll(buffer, (i) -> UShort.valueOf(0));
        Assert.assertFalse("Arrays shouldn't be equal after the original has been modified.", Arrays.deepEquals(buffer, clone));
    }
}
