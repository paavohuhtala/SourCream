
package paavohuh.sourcream.tests;

import org.joou.UByte;
import org.joou.UShort;
import org.junit.Assert;
import org.junit.Test;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.emulation.State;

public class StateTest extends TestWithState {
    
    @Test
    public void programCounterStartsAt0x200() {
        Assert.assertEquals(UShort.valueOf(0x200), initialState.getProgramCounter());
    }
    
    @Test
    public void canIncrementProgramCounter() {
        State state = initialState.withIncrementedPc();
        Assert.assertEquals(UShort.valueOf(0x202), state.getProgramCounter());
    }
    
    @Test
    public void addressRegisterStartsAt0() {
        Assert.assertEquals(UShort.valueOf(0x0000), initialState.getAddressRegister());
    }
        
    @Test
    public void canSetAddressRegister() {
        State state = initialState.withAddressRegister(UShort.valueOf(0x4004));
        Assert.assertEquals(UShort.valueOf(0x4004), state.getAddressRegister());
    }
    
    @Test
    public void ramStartsEmptyExceptForSystemFont() {
        // System fonts take the first 5 * 16 bytes.
        for (int i = 5 * 16; i < DeviceConfiguration.getDefault().getRamSize(); i++) {
            Assert.assertEquals(UByte.valueOf(0), initialState.get8BitsAt(UShort.valueOf(i)));
        }
    }
}
