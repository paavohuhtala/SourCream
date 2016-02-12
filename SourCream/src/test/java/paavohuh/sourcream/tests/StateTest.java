
package paavohuh.sourcream.tests;

import org.joou.UByte;
import org.joou.UShort;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.emulation.State;

public class StateTest {
    private State initialState;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.initialState = new State(DeviceConfiguration.getDefault());
    }
    
    @After
    public void tearDown() {
    }
    
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
    public void ramStartsEmpty() {
        for (int i = 0; i < DeviceConfiguration.getDefault().ramSize; i++) {
            Assert.assertEquals(UByte.valueOf(0), initialState.get8BitsAt(UShort.valueOf(i)));
        }
    }
}