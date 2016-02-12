package paavohuh.sourcream.tests.instructions;

import org.joou.UByte;
import org.joou.UShort;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import paavohuh.sourcream.configuration.DeviceConfiguration;
import paavohuh.sourcream.emulation.Instruction;
import paavohuh.sourcream.emulation.Register;
import paavohuh.sourcream.emulation.State;
import paavohuh.sourcream.emulation.instructions.Control;

public class ControlTest {
    private State initialState;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        initialState = new State(DeviceConfiguration.getDefault());
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void skipIfEqualsHasValidCode() {
        Instruction instr = new Control.SkipIfEquals(Register.VA, UByte.valueOf(0xFF));
        Assert.assertEquals(UShort.valueOf(0x3AFF), instr.getCode());
    }
}
