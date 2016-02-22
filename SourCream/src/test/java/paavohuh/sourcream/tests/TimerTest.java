
package paavohuh.sourcream.tests;

import org.junit.Assert;
import org.junit.Test;
import paavohuh.sourcream.emulation.Timer;

public class TimerTest {
    
    @Test
    public void basicTest() throws InterruptedException {
        Timer timer = new Timer(5, 100);
        Assert.assertEquals(5, timer.getValue());
        
        timer.setValue(10);
        Assert.assertEquals(10, timer.getValue());
        
        timer.start();
        Thread.sleep(200);
        timer.stop();
        Assert.assertEquals(0, timer.getValue());
    }
}
