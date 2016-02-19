
package paavohuh.sourcream.tests;

import org.junit.Assert;
import org.junit.Test;
import paavohuh.sourcream.emulation.ScreenBuffer;
import paavohuh.sourcream.emulation.ScreenBufferLoader;

public class ScreenBufferTest {
    
    @Test
    public void clearWorks() {
        ScreenBuffer buffer = ScreenBufferLoader.tryLoad("1111\n1111\n1111").get();
        ScreenBuffer cleared = buffer.cleared();
        
        Assert.assertEquals(buffer.width, cleared.width);
        Assert.assertEquals(buffer.height, cleared.height);
        Assert.assertTrue("'Flipped' should be set after clear", cleared.flipped);
        Assert.assertTrue("Should be modified after clear", cleared.modified);
        
        for (int y = 0; y < cleared.height; y++) {
            for (int x = 0; x < cleared.width; x++) {
                if(cleared.get(x, y)) {
                    Assert.fail("Screen contains a filled pixel: " + x + ", " + y);
                }
            }
        }
    }
}
