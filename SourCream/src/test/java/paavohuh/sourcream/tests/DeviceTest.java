
package paavohuh.sourcream.tests;

import org.joou.UShort;
import org.junit.Test;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.emulation.Device;
import paavohuh.sourcream.emulation.ProgramBuilder;
import paavohuh.sourcream.emulation.instructions.Control;

public class DeviceTest {
    
    @Test
    public void canCreateDevice() {
        Device device = new Device(DeviceConfiguration.getDefault(), new byte[0]);
    }
    
    @Test
    public void canRunDevice() throws InterruptedException {
        byte[] loop = ProgramBuilder.assemble(new Control.JumpTo(UShort.valueOf(0x200)));
        Device device = new Device(DeviceConfiguration.getDefault(), loop);
        
        Thread cpuThread = device.start();
        Thread.sleep(100);
        cpuThread.join(1000);
    }
}
